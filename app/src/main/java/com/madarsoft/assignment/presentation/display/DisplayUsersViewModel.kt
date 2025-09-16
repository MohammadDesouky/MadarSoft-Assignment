package com.madarsoft.assignment.presentation.display

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madarsoft.assignment.R
import com.madarsoft.assignment.common.StringProvider
import com.madarsoft.assignment.domain.usecase.GetAllUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DisplayUsersViewModel @Inject constructor(
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val stringProvider: StringProvider
) : ViewModel() {

    private val _state = MutableStateFlow(DisplayUsersState())
    val state: StateFlow<DisplayUsersState> = _state.asStateFlow()

    init {
        handleIntent(DisplayUsersIntent.LoadUsers)
    }

    fun handleIntent(intent: DisplayUsersIntent) {
        when (intent) {
            is DisplayUsersIntent.LoadUsers -> loadUsers()
            is DisplayUsersIntent.ClearError -> clearError()
        }
    }

    private fun loadUsers() {
        _state.value = _state.value.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            getAllUsersUseCase()
                .catch { exception ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: stringProvider.getString(R.string.error_load_users_failed)
                    )
                }
                .collect { users ->
                    _state.value = _state.value.copy(
                        users = users,
                        isLoading = false,
                        errorMessage = null
                    )
                }
        }
    }

    private fun clearError() {
        _state.value = _state.value.copy(errorMessage = null)
    }
}