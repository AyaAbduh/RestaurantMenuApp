package com.example.restaurantmenuapp

import com.example.restaurantmenuapp.data.NetworkUtil
import com.example.restaurantmenuapp.data.local.MenuDao
import com.example.restaurantmenuapp.data.model.Menu
import com.example.restaurantmenuapp.data.model.MenuItem
import com.example.restaurantmenuapp.data.model.MenuResponse
import com.example.restaurantmenuapp.data.model.Record
import com.example.restaurantmenuapp.data.remote.MenuApiService
import com.example.restaurantmenuapp.domain.menulist.MenuItemRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class MenuItemRepositoryTest {

    private val api: MenuApiService = mock(MenuApiService::class.java)
    private val dao: MenuDao = mock(MenuDao::class.java)

    @Test
    fun online_fetches_flattens_caches_and_returns_list() = runTest {
        // Simulate online
        NetworkUtil.isConnected = true

        val burgers = mutableListOf(MenuItem(
            id = 101, name = "Classic Beef Burger", price = 8.99, category = "hamburgers",
            description = TODO(),
            image = TODO(),
            rating = TODO(),
            vegan = TODO(),
            hot = TODO(),
            available = TODO()
        ))
        val drinks  = mutableListOf(MenuItem(
            id = 301, name = "Lemon Iced Tea", price = 3.50, category = "drinks",
            description = TODO(),
            image = TODO(),
            rating = TODO(),
            vegan = TODO(),
            hot = TODO(),
            available = TODO()
        ))

        whenever(api.getMenu()).thenReturn(Response.success(MenuResponse(record = Record( Menu(hamburgers = burgers, drinks = drinks)))))
        whenever(dao.clearAll()).thenReturn(Unit)
        whenever(dao.insertAll(any())).thenReturn(Unit)

        val repo = MenuItemRepository(api, dao)
        val result = repo.getMenuItems()

        assertEquals(2, result.size)
        // categories must be set by repo during flatten
        assertEquals(setOf("hamburgers", "drinks"), result.map { it.category }.toSet())

        verify(dao).clearAll()
        verify(dao).insertAll(result)
    }

    @Test
    fun offline_returns_cache() = runTest {
        // Simulate offline
        NetworkUtil.isConnected = false

        val cached = listOf(
            MenuItem(
                id = 202, name = "Arrabbiata Penne", price = 9.75, category = "pasta",
                description = TODO(),
                image = TODO(),
                rating = TODO(),
                vegan = TODO(),
                hot = TODO(),
                available = TODO()
            )
        )
        whenever(dao.getAll()).thenReturn(cached)

        val repo = MenuItemRepository(api, dao)
        val result = repo.getMenuItems()

        assertEquals(cached, result)
        // No network insert when offline
        verify(dao, never()).insertAll(any())
    }

    @Test
    fun online_null_body_falls_back_to_cache() = runTest {
        NetworkUtil.isConnected = true

        whenever(api.getMenu()).thenReturn(Response.success(null))
        val cached = listOf(MenuItem(
            id = 401, name = "Chicken Club Sandwich", price = 7.95, category = "sandwiches",
            description = TODO(),
            image = TODO(),
            rating = TODO(),
            vegan = TODO(),
            hot = TODO(),
            available = TODO()
        ))
        whenever(dao.getAll()).thenReturn(cached)

        val repo = MenuItemRepository(api, dao)
        val result = repo.getMenuItems()

        assertEquals(cached, result)
        verify(dao, never()).insertAll(any())
    }
}
