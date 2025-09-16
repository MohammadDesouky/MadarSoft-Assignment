package com.madarsoft.assignment.domain.model

data class UserModel(
    val id: Long = 0,
    val name: String,
    val age: Int,
    val jobTitle: String,
    val gender: String
)