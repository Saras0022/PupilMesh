package com.saras.pupilmesh.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.Query
import com.saras.pupilmesh.data.local.entity.NetworkDataEntity

@Dao
interface NetworkDataDao {
    @Query("SELECT * FROM network_data")
    suspend fun getAllData(): List<NetworkDataEntity>

    @Insert(onConflict = IGNORE)
    suspend fun insert(networkDataEntity: NetworkDataEntity)

    @Query("SELECT * FROM network_data WHERE id=:id")
    suspend fun getById(id: String): NetworkDataEntity
}