package com.madarsoft.assignment.data.mapper

import com.madarsoft.assignment.data.local.entity.User
import com.madarsoft.assignment.domain.model.UserModel

fun UserModel.toEntity(): User = User(
    id = id,
    name = name,
    age = age,
    jobTitle = jobTitle,
    gender = gender
)

fun User.toDomain(): UserModel = UserModel(
    id = id,
    name = name,
    age = age,
    jobTitle = jobTitle,
    gender = gender
)

fun List<User>.toDomain(): List<UserModel> = map { it.toDomain() }