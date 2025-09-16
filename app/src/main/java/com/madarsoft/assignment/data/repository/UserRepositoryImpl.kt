package com.madarsoft.assignment.data.repository

import com.madarsoft.assignment.data.local.dao.UserDao
import com.madarsoft.assignment.data.mapper.toDomain
import com.madarsoft.assignment.data.mapper.toEntity
import com.madarsoft.assignment.domain.model.UserModel
import com.madarsoft.assignment.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {

    override suspend fun insertUser(user: UserModel): Result<Long> {
        return try {
            val id = userDao.insertUser(user.toEntity())
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getAllUsers(): Flow<List<UserModel>> {
        return userDao.getAllUsers().map { users ->
            users.toDomain()
        }
    }
}