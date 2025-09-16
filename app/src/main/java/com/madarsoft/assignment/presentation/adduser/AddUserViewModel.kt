package com.madarsoft.assignment.presentation.adduser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madarsoft.assignment.R
import com.madarsoft.assignment.common.StringProvider
import com.madarsoft.assignment.domain.model.UserModel
import com.madarsoft.assignment.domain.usecase.InsertUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddUserViewModel @Inject constructor(
    private val insertUserUseCase: InsertUserUseCase,
    private val stringProvider: StringProvider
) : ViewModel() {

    private val _state = MutableStateFlow(AddUserState())
    val state: StateFlow<AddUserState> = _state.asStateFlow()

    fun handleIntent(intent: AddUserIntent) {
        when (intent) {
            is AddUserIntent.UpdateName -> {
                _state.update { it.copy(name = intent.name, errorMessage = null) }
            }
            is AddUserIntent.UpdateAge -> {
                if (intent.age.isEmpty() || intent.age.all { it.isDigit() }) {
                    _state.update { it.copy(age = intent.age, errorMessage = null) }
                }
            }
            is AddUserIntent.UpdateJobTitle -> {
                _state.update { it.copy(jobTitle = intent.jobTitle, errorMessage = null) }
            }
            is AddUserIntent.UpdateGender -> {
                _state.update { it.copy(gender = intent.gender, errorMessage = null) }
            }
            is AddUserIntent.SaveUser -> saveUser()
            is AddUserIntent.ClearError -> clearError()
            is AddUserIntent.ResetForm -> resetForm()
        }
    }

    private fun saveUser() {
        val currentState = _state.value

        if (currentState.name.isBlank()) {
            _state.update {
                it.copy(errorMessage = stringProvider.getString(R.string.error_name_required))
            }
            return
        }

        val ageInt = currentState.age.toIntOrNull()
        if (ageInt == null || ageInt !in 1..150) {
            _state.update {
                it.copy(errorMessage = stringProvider.getString(R.string.error_age_required))
            }
            return
        }

        if (currentState.jobTitle.isBlank()) {
            _state.update {
                it.copy(errorMessage = stringProvider.getString(R.string.error_job_title_required))
            }
            return
        }

        if (currentState.gender.isBlank()) {
            _state.update {
                it.copy(errorMessage = stringProvider.getString(R.string.error_gender_required))
            }
            return
        }

        _state.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            val user = UserModel(
                name = currentState.name.trim(),
                age = ageInt,
                jobTitle = currentState.jobTitle.trim(),
                gender = currentState.gender.trim()
            )

            insertUserUseCase(user)
                .onSuccess {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isUserSaved = true
                        )
                    }
                }
                .onFailure { exception ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = exception.message ?: stringProvider.getString(R.string.error_save_user_failed)
                        )
                    }
                }
        }
    }

    private fun clearError() {
        _state.update { it.copy(errorMessage = null) }
    }

    private fun resetForm() {
        _state.value = AddUserState()
    }
}