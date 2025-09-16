package com.madarsoft.assignment.presentation.adduser

sealed class AddUserIntent {
    data class UpdateName(val name: String) : AddUserIntent()
    data class UpdateAge(val age: String) : AddUserIntent()
    data class UpdateJobTitle(val jobTitle: String) : AddUserIntent()
    data class UpdateGender(val gender: String) : AddUserIntent()
    object SaveUser : AddUserIntent()
    object ClearError : AddUserIntent()
    object ResetForm : AddUserIntent()
}