package com.madarsoft.assignment.domain.repository

import com.madarsoft.assignment.domain.model.UserModel
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun insertUser(user: UserModel): Result<Long>
    fun getAllUsers(): Flow<List<UserModel>>
}