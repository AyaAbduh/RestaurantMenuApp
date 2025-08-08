package com.example.restaurantmenuapp.domain.menulist

import android.util.Log
import com.example.restaurantmenuapp.data.NetworkUtil
import com.example.restaurantmenuapp.data.local.MenuDao
import com.example.restaurantmenuapp.data.model.MenuItem
import com.example.restaurantmenuapp.data.remote.MenuApiService
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MenuItemRepository@Inject constructor(
    private val api: MenuApiService,
    private val dao: MenuDao,) {


    suspend fun getMenuItems(): List<MenuItem> {
        return if (NetworkUtil.isConnected) {
            try {
                Log.d("MenuItemRepositoryyyyy",NetworkUtil.isConnected.toString())
                val response = api.getMenu()
                Log.d("MenuItemRepositoryyyyy","item"+response.body()?.record?.menu?.hamburgers?.get(0)?.name)
                val allItems: List<MenuItem> = buildList {
                    addAll(response.body()?.record?.menu?.hamburgers.orEmpty().map { it.copy(category = "hamburgers") })
                    addAll(response.body()?.record?.menu?.pasta.orEmpty().map { it.copy(category = "pasta") })
                    addAll(response.body()?.record?.menu?.drinks.orEmpty().map { it.copy(category = "drinks") })
                    addAll(response.body()?.record?.menu?.sandwiches.orEmpty().map { it.copy(category = "sandwiches") })
                }
                Log.d("MenuItemRepositoryyyyy","item"+allItems.count())
                dao.clearAll()
                dao.insertAll(allItems)
                allItems
            } catch (e: Exception) {
                dao.getAll()
            }
        } else {
            dao.getAll()
        }
    }




}