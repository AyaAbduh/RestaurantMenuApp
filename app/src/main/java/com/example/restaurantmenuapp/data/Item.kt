package com.example.restaurantmenuapp.data

data class Item(
    val id : Int,
    val name:String,
    val price: Double,
    val description:String,
    val vegan:Boolean,
    val hot:Boolean,
    val rating:Double,
    val image:String,
    val available:Boolean
)
