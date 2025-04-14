package com.saras.pupilmesh.di

import android.content.Context
import androidx.room.Room
import com.saras.network.KtorClient
import com.saras.pupilmesh.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class Module {

    @Provides
    @Singleton
    fun provideKtorClient() = KtorClient()

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideNetworkDataDao(database: AppDatabase) = database.networkDataDao()

    @Provides
    @Singleton
    fun provideUserDao(database: AppDatabase) = database.userDao()
}