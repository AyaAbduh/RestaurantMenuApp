package com.example.restaurantmenuapp.data.local


import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module @InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides @Singleton
    fun db(@ApplicationContext ctx: Context): MenuDatabase =
        Room.databaseBuilder(ctx, MenuDatabase::class.java, "menu_db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides fun dao(db: MenuDatabase): MenuDao = db.menuDao()
}

