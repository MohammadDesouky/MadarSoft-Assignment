package com.madarsoft.assignment.data.mapper

import com.madarsoft.assignment.data.local.entity.User
import com.madarsoft.assignment.domain.model.UserModel
import org.junit.Test
import kotlin.test.assertEquals

class UserMapperTest {

    @Test
    fun `UserModel toEntity should map correctly`() {
        // Given
        val userModel = UserModel(
            id = 1,
            name = "John Doe",
            age = 30,
            jobTitle = "Software Engineer",
            gender = "Male"
        )

        // When
        val entity = userModel.toEntity()

        // Then
        assertEquals(userModel.id, entity.id)
        assertEquals(userModel.name, entity.name)
        assertEquals(userModel.age, entity.age)
        assertEquals(userModel.jobTitle, entity.jobTitle)
        assertEquals(userModel.gender, entity.gender)
    }

    @Test
    fun `User toDomain should map correctly`() {
        // Given
        val user = User(
            id = 1,
            name = "Jane Smith",
            age = 25,
            jobTitle = "Designer",
            gender = "Female"
        )

        // When
        val domainModel = user.toDomain()

        // Then
        assertEquals(user.id, domainModel.id)
        assertEquals(user.name, domainModel.name)
        assertEquals(user.age, domainModel.age)
        assertEquals(user.jobTitle, domainModel.jobTitle)
        assertEquals(user.gender, domainModel.gender)
    }

    @Test
    fun `List of User toDomain should map correctly`() {
        // Given
        val users = listOf(
            User(
                id = 1,
                name = "John Doe",
                age = 30,
                jobTitle = "Software Engineer",
                gender = "Male"
            ),
            User(
                id = 2,
                name = "Jane Smith",
                age = 25,
                jobTitle = "Designer",
                gender = "Female"
            )
        )

        // When
        val domainModels = users.toDomain()

        // Then
        assertEquals(users.size, domainModels.size)
        users.forEachIndexed { index, user ->
            assertEquals(user.id, domainModels[index].id)
            assertEquals(user.name, domainModels[index].name)
            assertEquals(user.age, domainModels[index].age)
            assertEquals(user.jobTitle, domainModels[index].jobTitle)
            assertEquals(user.gender, domainModels[index].gender)
        }
    }

    @Test
    fun `empty list toDomain should return empty list`() {
        // Given
        val emptyUserList = emptyList<User>()

        // When
        val domainModels = emptyUserList.toDomain()

        // Then
        assertEquals(0, domainModels.size)
    }
}