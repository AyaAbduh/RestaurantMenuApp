package com.example.restaurantmenuapp.data.remote
import com.example.restaurantmenuapp.data.model.MenuResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface MenuApiService {

    //list of movies
    @GET("b/6882874df7e7a370d1ed6cc1/latest")
    suspend fun getMenu(
    ): Response<MenuResponse>



}