package com.example.restaurantmenuapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurantmenuapp.data.model.MenuItem
import com.example.restaurantmenuapp.domain.menulist.MenuItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val repo: MenuItemRepository
) : ViewModel() {

    private val _items = MutableStateFlow<List<MenuItem>>(emptyList())
    val items: StateFlow<List<MenuItem>> = _items

    fun load() {
        viewModelScope.launch {
            _items.value = repo.getMenuItems()
        }
    }
}
