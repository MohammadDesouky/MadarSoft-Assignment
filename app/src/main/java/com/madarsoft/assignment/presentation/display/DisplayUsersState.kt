package com.madarsoft.assignment.presentation.display

import com.madarsoft.assignment.domain.model.UserModel

data class DisplayUsersState(
    val users: List<UserModel> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)