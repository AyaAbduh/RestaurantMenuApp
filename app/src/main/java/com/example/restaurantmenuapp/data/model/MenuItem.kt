package com.example.restaurantmenuapp.data.model


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "menu_items")
data class MenuItem(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val image: String,
    val rating: Float,
    val vegan: Boolean,
    val hot: Boolean,
    val available: Boolean,
    var category: String = ""
    )
