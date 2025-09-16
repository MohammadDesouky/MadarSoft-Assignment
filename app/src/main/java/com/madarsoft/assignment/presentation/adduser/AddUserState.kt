package com.madarsoft.assignment.presentation.adduser

data class AddUserState(
    val name: String = "",
    val age: String = "",
    val jobTitle: String = "",
    val gender: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isUserSaved: Boolean = false
)