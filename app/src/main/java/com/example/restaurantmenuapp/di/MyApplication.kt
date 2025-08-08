package com.example.restaurantmenuapp.di

import android.app.Application
import com.example.restaurantmenuapp.data.NetworkUtil
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        NetworkUtil.startNetworkMonitoring(this)
    }
}