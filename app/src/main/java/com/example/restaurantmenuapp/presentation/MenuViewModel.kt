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

    private val _all = MutableStateFlow<List<MenuItem>>(emptyList())
    private val _items = MutableStateFlow<List<MenuItem>>(emptyList())
    val items: StateFlow<List<MenuItem>> = _items

    // load once
    fun load() {
        viewModelScope.launch {
            val data = repo.getMenuItems()
            _all.value = data
            _items.value = data
        }
    }

    // super simple sorting API the Screen will call
    fun sortItemsBy(option: String) {
        val list = _all.value
        _items.value = when (option) {
            "Price ↑"   -> list.sortedBy { it.price }
            "Price ↓"   -> list.sortedByDescending { it.price }
            "Rating ↑"  -> list.sortedBy { it.rating }
            "Rating ↓"  -> list.sortedByDescending { it.rating }
            "Vegan"     -> list.sortedByDescending { it.vegan }       // vegan first
            "Hot"       -> list.sortedByDescending { it.hot }         // hot first
            "Available" -> list.sortedByDescending { it.available }   // available first
            else        -> list
        }
    }
}
