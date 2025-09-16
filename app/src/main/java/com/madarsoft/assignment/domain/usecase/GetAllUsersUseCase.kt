package com.madarsoft.assignment.domain.usecase

import com.madarsoft.assignment.domain.model.UserModel
import com.madarsoft.assignment.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<List<UserModel>> {
        return userRepository.getAllUsers()
    }
}