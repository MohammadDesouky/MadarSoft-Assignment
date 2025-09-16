package com.madarsoft.assignment.domain.usecase

import com.madarsoft.assignment.domain.model.UserModel
import com.madarsoft.assignment.domain.repository.UserRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class GetAllUsersUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var getAllUsersUseCase: GetAllUsersUseCase

    @Before
    fun setUp() {
        userRepository = mock()
        getAllUsersUseCase = GetAllUsersUseCase(userRepository)
    }

    @Test
    fun `invoke should return users from repository`() = runTest {
        // Given
        val users = listOf(
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
        whenever(userRepository.getAllUsers()).thenReturn(flowOf(users))

        // When
        val result = getAllUsersUseCase().toList()

        // Then
        assertEquals(1, result.size)
        assertEquals(users, result[0])
        verify(userRepository).getAllUsers()
    }

    @Test
    fun `invoke with empty list should return empty list`() = runTest {
        // Given
        val emptyList = emptyList<UserModel>()
        whenever(userRepository.getAllUsers()).thenReturn(flowOf(emptyList))

        // When
        val result = getAllUsersUseCase().toList()

        // Then
        assertEquals(1, result.size)
        assertEquals(emptyList, result[0])
        verify(userRepository).getAllUsers()
    }
}