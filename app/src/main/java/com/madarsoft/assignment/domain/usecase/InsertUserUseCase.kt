package com.madarsoft.assignment.domain.usecase

import com.madarsoft.assignment.domain.model.UserModel
import com.madarsoft.assignment.domain.repository.UserRepository
import javax.inject.Inject

class InsertUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: UserModel): Result<Long> {
        return if (isValidUser(user)) {
            userRepository.insertUser(user)
        } else {
            Result.failure(IllegalArgumentException("Invalid user data"))
        }
    }

    private fun isValidUser(user: UserModel): Boolean {
        return user.name.isNotBlank() &&
                user.age > 0 &&
                user.jobTitle.isNotBlank() &&
                user.gender.isNotBlank()
    }
}