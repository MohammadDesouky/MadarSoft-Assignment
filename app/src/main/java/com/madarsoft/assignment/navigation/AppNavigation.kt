package com.madarsoft.assignment.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.madarsoft.assignment.presentation.adduser.AddUserIntent
import com.madarsoft.assignment.presentation.adduser.AddUserScreen
import com.madarsoft.assignment.presentation.adduser.AddUserViewModel
import com.madarsoft.assignment.presentation.display.DisplayUsersScreen
import com.madarsoft.assignment.presentation.display.DisplayUsersViewModel

sealed class Screen(val route: String) {
    object AddUser : Screen("add_user")
    object DisplayUsers : Screen("display_users")
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.DisplayUsers.route
    ) {
        composable(Screen.AddUser.route) {
            val viewModel: AddUserViewModel = hiltViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle()

            LaunchedEffect(state.isUserSaved) {
                if (state.isUserSaved) {
                    navController.navigate(Screen.DisplayUsers.route) {
                        popUpTo(Screen.AddUser.route) { inclusive = true }
                    }
                    viewModel.handleIntent(AddUserIntent.ResetForm)
                }
            }

            AddUserScreen(
                state = state,
                onNameChange = { viewModel.handleIntent(AddUserIntent.UpdateName(it)) },
                onAgeChange = { viewModel.handleIntent(AddUserIntent.UpdateAge(it)) },
                onJobTitleChange = { viewModel.handleIntent(AddUserIntent.UpdateJobTitle(it)) },
                onGenderChange = { viewModel.handleIntent(AddUserIntent.UpdateGender(it)) },
                onSaveUser = { viewModel.handleIntent(AddUserIntent.SaveUser) },
                onNavigateToDisplay = {
                    navController.navigate(Screen.DisplayUsers.route)
                }
            )
        }

        composable(Screen.DisplayUsers.route) {
            val viewModel: DisplayUsersViewModel = hiltViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle()

            DisplayUsersScreen(
                state = state,
                onNavigateToAddUser = {
                    navController.navigate(Screen.AddUser.route)
                }
            )
        }
    }
}