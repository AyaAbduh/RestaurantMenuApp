package com.example.restaurantmenuapp.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Collections
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.X509TrustManager

const val BASE_URL = "https://api.jsonbin.io"


@InstallIn(SingletonComponent::class)
@Module
object RetrofitModule {

    @Provides
    @Singleton
    fun getClient(): TheNetworkInterface {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(80, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .header("X-Master-Key", "\$2a\$10\$gKGk.WhhPDqDbokEJkqkUucnE66bIZSSTGM31bo6SUkYjYbRD2Hf6")
//                    .method(original.method, original.body)
                    .build()

                chain.proceed(request)

            }.build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TheNetworkInterface::class.java)
    }
}
