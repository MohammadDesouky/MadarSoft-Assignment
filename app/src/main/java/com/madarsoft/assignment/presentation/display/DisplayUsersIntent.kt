package com.madarsoft.assignment.presentation.display

sealed class DisplayUsersIntent {
    object LoadUsers : DisplayUsersIntent()
    object ClearError : DisplayUsersIntent()
}