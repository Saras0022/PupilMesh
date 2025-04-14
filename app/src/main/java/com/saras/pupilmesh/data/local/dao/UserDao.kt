package com.saras.pupilmesh.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.saras.pupilmesh.data.local.entity.UserEntity

@Dao
interface UserDao {

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Insert
    suspend fun insert(user: UserEntity)
}