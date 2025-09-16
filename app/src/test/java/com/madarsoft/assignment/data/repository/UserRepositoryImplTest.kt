package com.madarsoft.assignment.data.repository

import com.madarsoft.assignment.data.local.dao.UserDao
import com.madarsoft.assignment.data.local.entity.User
import com.madarsoft.assignment.domain.model.UserModel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UserRepositoryImplTest {

    private lateinit var userDao: UserDao
    private lateinit var userRepository: UserRepositoryImpl

    @Before
    fun setUp() {
        userDao = mock()
        userRepository = UserRepositoryImpl(userDao)
    }

    @Test
    fun `insertUser with valid user should return success`() = runTest {
        // Given
        val userModel = UserModel(
            name = "John Doe",
            age = 30,
            jobTitle = "Software Engineer",
            gender = "Male"
        )
        val expectedId = 1L
        val userEntity = User(
            id = 0,
            name = "John Doe",
            age = 30,
            jobTitle = "Software Engineer",
            gender = "Male"
        )
        whenever(userDao.insertUser(userEntity)).thenReturn(expectedId)

        // When
        val result = userRepository.insertUser(userModel)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedId, result.getOrNull())
        verify(userDao).insertUser(userEntity)
    }

    @Test
    fun `insertUser with dao exception should return failure`() = runTest {
        // Given
        val userModel = UserModel(
            name = "John Doe",
            age = 30,
            jobTitle = "Software Engineer",
            gender = "Male"
        )
        val userEntity = User(
            id = 0,
            name = "John Doe",
            age = 30,
            jobTitle = "Software Engineer",
            gender = "Male"
        )
        val exception = RuntimeException("Database error")
        whenever(userDao.insertUser(userEntity)).thenThrow(exception)

        // When
        val result = userRepository.insertUser(userModel)

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
        verify(userDao).insertUser(userEntity)
    }

    @Test
    fun `getAllUsers should return mapped users from dao`() = runTest {
        // Given
        val userEntities = listOf(
            User(
                id = 1,
                name = "John Doe",
                age = 30,
                jobTitle = "Software Engineer",
                gender = "Male"
            ),
            User(
                id = 2,
                name = "Jane Smith",
                age = 25,
                jobTitle = "Designer",
                gender = "Female"
            )
        )
        val expectedUserModels = listOf(
            UserModel(
                id = 1,
                name = "John Doe",
                age = 30,
                jobTitle = "Software Engineer",
                gender = "Male"
            ),
            UserModel(
                id = 2,
                name = "Jane Smith",
                age = 25,
                jobTitle = "Designer",
                gender = "Female"
            )
        )
        whenever(userDao.getAllUsers()).thenReturn(flowOf(userEntities))

        // When
        val result = userRepository.getAllUsers().toList()

        // Then
        assertEquals(1, result.size)
        assertEquals(expectedUserModels, result[0])
        verify(userDao).getAllUsers()
    }

    @Test
    fun `getAllUsers with empty list should return empty list`() = runTest {
        // Given
        val emptyList = emptyList<User>()
        whenever(userDao.getAllUsers()).thenReturn(flowOf(emptyList))

        // When
        val result = userRepository.getAllUsers().toList()

        // Then
        assertEquals(1, result.size)
        assertEquals(emptyList<UserModel>(), result[0])
        verify(userDao).getAllUsers()
    }
}