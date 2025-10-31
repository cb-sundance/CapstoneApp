package com.example.capstoneapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {

    private lateinit var dataStoreManager: DataStoreManager
    private val appViewModel: AppViewModel by viewModels {
        AppViewModelFactory(dataStoreManager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataStoreManager = DataStoreManager(this)

        setContent {
            MyApp(appViewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp(viewModel: AppViewModel) {
    val navController = rememberNavController()

    val userName = viewModel.userName.collectAsState(initial = "")
    val isDarkMode = viewModel.isDarkMode.collectAsState(initial = false)

    val backgroundColor = if (isDarkMode.value) Color(0xFF121212) else Color(0xFFFFFFFF)
    val topBarColor = if (isDarkMode.value) Color(0xFF1F1F1F) else Color(0xFF1976D2)

    Scaffold(
        bottomBar = { BottomNavBar(navController, userName.value) },
        containerColor = backgroundColor
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "welcome",
            modifier = androidx.compose.ui.Modifier.padding(paddingValues)
        ) {
            composable("welcome") {
                WelcomeScreen(
                    navController = navController,
                    userName = userName.value,
                    onUserNameChange = { viewModel.setUserName(it) },
                    isDarkMode = isDarkMode.value,
                    toggleDarkMode = { viewModel.setDarkMode(it) },
                    topBarColor = topBarColor
                )
            }
            composable("aboutMe") {
                AboutMeScreen(
                    userName = userName.value,
                    navController = navController,
                    isDarkMode = isDarkMode.value,
                    toggleDarkMode = { viewModel.setDarkMode(it) },
                    topBarColor = topBarColor
                )
            }
            composable("funFacts") {
                FunFactsScreen(
                    navController = navController,
                    isDarkMode = isDarkMode.value,
                    toggleDarkMode = { viewModel.setDarkMode(it) },
                    topBarColor = topBarColor
                )
            }
        }
    }
}
