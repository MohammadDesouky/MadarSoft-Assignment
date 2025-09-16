package com.madarsoft.assignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.madarsoft.assignment.navigation.AppNavigation
import com.madarsoft.assignment.ui.theme.MadarSoftAssignmentTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MadarSoftAssignmentTheme {
                AppNavigation()
            }
        }
    }
}