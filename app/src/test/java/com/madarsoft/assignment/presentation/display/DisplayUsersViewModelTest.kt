package com.madarsoft.assignment.presentation.display

import app.cash.turbine.test
import com.madarsoft.assignment.R
import com.madarsoft.assignment.common.StringProvider
import com.madarsoft.assignment.domain.model.UserModel
import com.madarsoft.assignment.domain.usecase.GetAllUsersUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class DisplayUsersViewModelTest {

    private lateinit var getAllUsersUseCase: GetAllUsersUseCase
    private lateinit var stringProvider: StringProvider
    private lateinit var viewModel: DisplayUsersViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getAllUsersUseCase = mock()
        stringProvider = mock()

        // Mock string provider response
        whenever(stringProvider.getString(R.string.error_load_users_failed)).thenReturn("Failed to load users")
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init should load users successfully`() = runTest {
        // Given
        val users = listOf(
            UserModel(
                id = 1,
                name = "John Doe",
                age = 30,
                jobTitle = "Software Engineer",
                gender = "Male"
            )
        )
        whenever(getAllUsersUseCase()).thenReturn(flowOf(users))

        // When
        viewModel = DisplayUsersViewModel(getAllUsersUseCase, stringProvider)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertEquals(users, state.users)
            assertFalse(state.isLoading)
            assertNull(state.errorMessage)
        }
        verify(getAllUsersUseCase).invoke()
    }

    @Test
    fun `init should handle empty users list`() = runTest {
        // Given
        whenever(getAllUsersUseCase()).thenReturn(flowOf(emptyList()))

        // When
        viewModel = DisplayUsersViewModel(getAllUsersUseCase, stringProvider)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertTrue(state.users.isEmpty())
            assertFalse(state.isLoading)
            assertNull(state.errorMessage)
        }
    }

    @Test
    fun `init should handle error from use case`() = runTest {
        // Given
        val exception = RuntimeException("Database error")
        whenever(getAllUsersUseCase()).thenReturn(flow { throw exception })

        // When
        viewModel = DisplayUsersViewModel(getAllUsersUseCase, stringProvider)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertTrue(state.users.isEmpty())
            assertFalse(state.isLoading)
            assertEquals("Database error", state.errorMessage)
        }
    }

    @Test
    fun `loadUsers intent should reload users`() = runTest {
        // Given
        val users = listOf(
            UserModel(
                id = 1,
                name = "John Doe",
                age = 30,
                jobTitle = "Software Engineer",
                gender = "Male"
            )
        )
        whenever(getAllUsersUseCase()).thenReturn(flowOf(users))
        viewModel = DisplayUsersViewModel(getAllUsersUseCase, stringProvider)
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.handleIntent(DisplayUsersIntent.LoadUsers)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertEquals(users, state.users)
            assertFalse(state.isLoading)
        }
    }

    @Test
    fun `clearError intent should clear error message`() = runTest {
        // Given
        whenever(getAllUsersUseCase()).thenReturn(flow { throw RuntimeException("Error") })
        viewModel = DisplayUsersViewModel(getAllUsersUseCase, stringProvider)
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.handleIntent(DisplayUsersIntent.ClearError)

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertNull(state.errorMessage)
        }
    }
}