package com.madarsoft.assignment.domain.usecase

import com.madarsoft.assignment.domain.model.UserModel
import com.madarsoft.assignment.domain.repository.UserRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class InsertUserUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var insertUserUseCase: InsertUserUseCase

    @Before
    fun setUp() {
        userRepository = mock()
        insertUserUseCase = InsertUserUseCase(userRepository)
    }

    @Test
    fun `invoke with valid user should return success`() = runTest {
        // Given
        val validUser = UserModel(
            name = "John Doe",
            age = 30,
            jobTitle = "Software Engineer",
            gender = "Male"
        )
        val expectedId = 1L
        whenever(userRepository.insertUser(validUser)).thenReturn(Result.success(expectedId))

        // When
        val result = insertUserUseCase(validUser)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedId, result.getOrNull())
        verify(userRepository).insertUser(validUser)
    }

    @Test
    fun `invoke with empty name should return failure`() = runTest {
        // Given
        val invalidUser = UserModel(
            name = "",
            age = 30,
            jobTitle = "Software Engineer",
            gender = "Male"
        )

        // When
        val result = insertUserUseCase(invalidUser)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `invoke with blank name should return failure`() = runTest {
        // Given
        val invalidUser = UserModel(
            name = "   ",
            age = 30,
            jobTitle = "Software Engineer",
            gender = "Male"
        )

        // When
        val result = insertUserUseCase(invalidUser)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `invoke with invalid age should return failure`() = runTest {
        // Given
        val invalidUser = UserModel(
            name = "John Doe",
            age = 0,
            jobTitle = "Software Engineer",
            gender = "Male"
        )

        // When
        val result = insertUserUseCase(invalidUser)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `invoke with negative age should return failure`() = runTest {
        // Given
        val invalidUser = UserModel(
            name = "John Doe",
            age = -5,
            jobTitle = "Software Engineer",
            gender = "Male"
        )

        // When
        val result = insertUserUseCase(invalidUser)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `invoke with empty job title should return failure`() = runTest {
        // Given
        val invalidUser = UserModel(
            name = "John Doe",
            age = 30,
            jobTitle = "",
            gender = "Male"
        )

        // When
        val result = insertUserUseCase(invalidUser)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `invoke with empty gender should return failure`() = runTest {
        // Given
        val invalidUser = UserModel(
            name = "John Doe",
            age = 30,
            jobTitle = "Software Engineer",
            gender = ""
        )

        // When
        val result = insertUserUseCase(invalidUser)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `invoke with repository failure should return failure`() = runTest {
        // Given
        val validUser = UserModel(
            name = "John Doe",
            age = 30,
            jobTitle = "Software Engineer",
            gender = "Male"
        )
        val exception = RuntimeException("Database error")
        whenever(userRepository.insertUser(validUser)).thenReturn(Result.failure(exception))

        // When
        val result = insertUserUseCase(validUser)

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
        verify(userRepository).insertUser(validUser)
    }
}