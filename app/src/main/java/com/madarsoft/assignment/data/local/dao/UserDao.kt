package com.madarsoft.assignment.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.madarsoft.assignment.data.local.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert
    suspend fun insertUser(user: User): Long

    @Query("SELECT * FROM users ORDER BY id DESC")
    fun getAllUsers(): Flow<List<User>>

    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()
}