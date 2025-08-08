package com.example.restaurantmenuapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.restaurantmenuapp.data.model.MenuItem


@Dao
interface MenuDao {
    @Query("SELECT * FROM menu_items")
    suspend fun getAll(): List<MenuItem>

    @Query("DELETE FROM menu_items")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<MenuItem>)
}
