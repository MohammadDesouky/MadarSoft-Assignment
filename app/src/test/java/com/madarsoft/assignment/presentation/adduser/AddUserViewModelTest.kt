package com.madarsoft.assignment.presentation.adduser

import app.cash.turbine.test
import com.madarsoft.assignment.R
import com.madarsoft.assignment.common.StringProvider
import com.madarsoft.assignment.domain.usecase.InsertUserUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class AddUserViewModelTest {

    private lateinit var insertUserUseCase: InsertUserUseCase
    private lateinit var stringProvider: StringProvider
    private lateinit var viewModel: AddUserViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        insertUserUseCase = mock()
        stringProvider = mock()

        // Mock string provider responses
        whenever(stringProvider.getString(R.string.error_name_required)).thenReturn("Name is required")
        whenever(stringProvider.getString(R.string.error_age_required)).thenReturn("Age must be a number between 1 and 150")
        whenever(stringProvider.getString(R.string.error_job_title_required)).thenReturn("Job title is required")
        whenever(stringProvider.getString(R.string.error_gender_required)).thenReturn("Gender is required")
        whenever(stringProvider.getString(R.string.error_save_user_failed)).thenReturn("Failed to save user")

        viewModel = AddUserViewModel(insertUserUseCase, stringProvider)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be default`() = runTest {
        // Then
        viewModel.state.test {
            val initialState = awaitItem()
            assertEquals(AddUserState(), initialState)
        }
    }

    @Test
    fun `updateName should update name and clear error`() = runTest {
        // Given
        val name = "John Doe"

        // When
        viewModel.handleIntent(AddUserIntent.UpdateName(name))

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertEquals(name, state.name)
            assertNull(state.errorMessage)
        }
    }

    @Test
    fun `updateAge with valid number should update age`() = runTest {
        // Given
        val age = "30"

        // When
        viewModel.handleIntent(AddUserIntent.UpdateAge(age))

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertEquals(age, state.age)
            assertNull(state.errorMessage)
        }
    }

    @Test
    fun `updateAge with empty string should update age`() = runTest {
        // Given
        val age = ""

        // When
        viewModel.handleIntent(AddUserIntent.UpdateAge(age))

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertEquals(age, state.age)
        }
    }

    @Test
    fun `updateAge with non-numeric input should not update age`() = runTest {
        // Given
        val initialAge = "25"
        viewModel.handleIntent(AddUserIntent.UpdateAge(initialAge))

        // When
        viewModel.handleIntent(AddUserIntent.UpdateAge("abc"))

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertEquals(initialAge, state.age)
        }
    }

    @Test
    fun `saveUser with empty name should show error`() = runTest {
        // When
        viewModel.handleIntent(AddUserIntent.SaveUser)

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertEquals("Name is required", state.errorMessage)
            assertFalse(state.isLoading)
        }
    }

    @Test
    fun `saveUser with invalid age should show error`() = runTest {
        // Given
        viewModel.handleIntent(AddUserIntent.UpdateName("John"))

        // When
        viewModel.handleIntent(AddUserIntent.SaveUser)

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertEquals("Age must be a number between 1 and 150", state.errorMessage)
        }
    }

    @Test
    fun `saveUser with valid data and successful insert should mark as saved`() = runTest {
        // Given
        whenever(insertUserUseCase(any())).thenReturn(Result.success(1L))

        viewModel.handleIntent(AddUserIntent.UpdateName("John Doe"))
        viewModel.handleIntent(AddUserIntent.UpdateAge("30"))
        viewModel.handleIntent(AddUserIntent.UpdateJobTitle("Engineer"))
        viewModel.handleIntent(AddUserIntent.UpdateGender("Male"))

        // When
        viewModel.handleIntent(AddUserIntent.SaveUser)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertTrue(state.isUserSaved)
            assertFalse(state.isLoading)
            assertNull(state.errorMessage)
        }
    }

    @Test
    fun `saveUser with failed insert should show error`() = runTest {
        // Given
        val exception = RuntimeException("Database error")
        whenever(insertUserUseCase(any())).thenReturn(Result.failure(exception))

        viewModel.handleIntent(AddUserIntent.UpdateName("John Doe"))
        viewModel.handleIntent(AddUserIntent.UpdateAge("30"))
        viewModel.handleIntent(AddUserIntent.UpdateJobTitle("Engineer"))
        viewModel.handleIntent(AddUserIntent.UpdateGender("Male"))

        // When
        viewModel.handleIntent(AddUserIntent.SaveUser)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertEquals("Database error", state.errorMessage)
            assertFalse(state.isLoading)
            assertFalse(state.isUserSaved)
        }
    }

    @Test
    fun `clearError should clear error message`() = runTest {
        // Given
        viewModel.handleIntent(AddUserIntent.SaveUser) // This will set an error

        // When
        viewModel.handleIntent(AddUserIntent.ClearError)

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertNull(state.errorMessage)
        }
    }

    @Test
    fun `resetForm should reset to initial state`() = runTest {
        // Given
        viewModel.handleIntent(AddUserIntent.UpdateName("John"))
        viewModel.handleIntent(AddUserIntent.UpdateAge("30"))

        // When
        viewModel.handleIntent(AddUserIntent.ResetForm)

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertEquals(AddUserState(), state)
        }
    }
}