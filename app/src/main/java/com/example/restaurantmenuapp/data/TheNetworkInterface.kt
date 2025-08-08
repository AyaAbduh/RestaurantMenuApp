package com.example.restaurantmenuapp.data
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheNetworkInterface {

    //list of movies
    @GET("v3/b/6882874df7e7a370d1ed6cc1/latest")
    suspend fun getMenu(
    ): Response<MenuResponse>



}