package com.saras.pupilmesh.data.repository

import com.saras.pupilmesh.data.local.dao.UserDao
import com.saras.pupilmesh.data.local.entity.UserEntity
import java.security.MessageDigest
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao
) {
    suspend fun signInOrCreateUser(email: String, password: String): Boolean {
        val user = userDao.getUserByEmail(email)
        val hashed = hashPassword(password)

        return if (user == null) {
            // Create new user
            userDao.insert(UserEntity(email, hashed))
            true
        } else {
            // Check password
            user.passwordHash == hashed
        }
    }
}

private fun hashPassword(password: String): String {
    return MessageDigest.getInstance("SHA-256")
        .digest(password.toByteArray())
        .joinToString("") { "%02x".format(it) }
}