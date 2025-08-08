package com.example.restaurantmenuapp

import app.cash.turbine.test
import com.example.restaurantmenuapp.data.model.MenuItem
import com.example.restaurantmenuapp.domain.menulist.MenuItemRepository
import com.example.restaurantmenuapp.presentation.MenuViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class MenuViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: MenuItemRepository
    private lateinit var viewModel: MenuViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mock(MenuItemRepository::class.java)
        viewModel = MenuViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `load should update items from repository`() = runTest {
        // Given
        val fakeList = listOf(
            MenuItem(1, "Burger", "desc", 10.0,"image.jpg", 4.5f ,true, false, true, "hamburgers", )
        )
        whenever(repository.getMenuItems()).thenReturn(fakeList)

        // When
        viewModel.load()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.items.test {
            val result = awaitItem()
            assertEquals(1, result.size)
            assertEquals("Burger", result[0].name)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
