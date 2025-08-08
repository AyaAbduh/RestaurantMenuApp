package com.example.restaurantmenuapp.data.model

data class Menu(
    val hamburgers: MutableList<MenuItem> =mutableListOf<MenuItem>(),
    val pasta:MutableList<MenuItem> =mutableListOf<MenuItem>(),
    val drinks:MutableList<MenuItem> =mutableListOf<MenuItem>(),
    val sandwiches:MutableList<MenuItem> =mutableListOf<MenuItem>()
)
