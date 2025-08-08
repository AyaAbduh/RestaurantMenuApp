package com.example.restaurantmenuapp.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.ConnectivityManager.NetworkCallback
import android.widget.Toast
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NetworkUtil {
    public var isConnected: Boolean = false
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkCallback: NetworkCallback

    fun startNetworkMonitoring(context: Context) {
        connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        networkCallback = object : NetworkCallback() {
            override fun onAvailable(network: Network) {
                isConnected = true
                Toast.makeText(context, isConnected.toString(),Toast.LENGTH_LONG).show()
            }

            override fun onLost(network: Network) {
                isConnected = false
                Toast.makeText(context, isConnected.toString(),Toast.LENGTH_LONG).show()

            }
        }
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    }

    fun stopNetworkMonitoring() {
        connectivityManager.let { cm ->
            networkCallback.let { cb ->
                cm.unregisterNetworkCallback(cb)
            }
        }
    }
}
