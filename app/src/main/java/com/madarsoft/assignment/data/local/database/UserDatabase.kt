package com.madarsoft.assignment.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.madarsoft.assignment.data.local.dao.UserDao
import com.madarsoft.assignment.data.local.entity.User

@Database(
    entities = [User::class],
    version = 1,
    exportSchema = false
)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        const val DATABASE_NAME = "user_database"
    }
}