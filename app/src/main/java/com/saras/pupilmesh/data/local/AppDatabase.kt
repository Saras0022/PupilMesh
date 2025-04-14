package com.saras.pupilmesh.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.saras.pupilmesh.data.local.dao.NetworkDataDao
import com.saras.pupilmesh.data.local.dao.UserDao
import com.saras.pupilmesh.data.local.entity.NetworkDataEntity
import com.saras.pupilmesh.data.local.entity.UserEntity

@Database(entities = [NetworkDataEntity::class, UserEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun networkDataDao(): NetworkDataDao
}