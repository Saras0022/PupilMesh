package com.saras.pupilmesh.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "network_data")
data class NetworkDataEntity(
    @PrimaryKey val id: String,
    val imageUrl: String,
    val title: String,
    val subTitle: String,
    val status: String,
    val summary: String,
)