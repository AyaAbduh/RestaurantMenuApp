package com.example.restaurantmenuapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.restaurantmenuapp.data.model.MenuItem


@Database(entities = [MenuItem::class], version = 1, exportSchema = false)
abstract class MenuDatabase : RoomDatabase() {
    abstract fun menuDao(): MenuDao
}