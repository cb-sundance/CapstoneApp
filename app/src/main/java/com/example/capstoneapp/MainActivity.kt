package com.example.capstoneapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {

    private lateinit var dataStoreManager: DataStoreManager
    private val appViewModel: AppViewModel by viewModels { AppViewModelFactory(dataStoreManager) }

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

    val userName by viewModel.userName.collectAsState()
    val isDarkMode by viewModel.isDarkMode.collectAsState()

    val backgroundColor = if (isDarkMode) androidx.compose.ui.graphics.Color(0xFF121212) else androidx.compose.ui.graphics.Color(0xFFFFFFFF)
    val topBarColor = if (isDarkMode) androidx.compose.ui.graphics.Color(0xFF1F1F1F) else androidx.compose.ui.graphics.Color(0xFF1976D2)

    Scaffold(
        bottomBar = { BottomNavBar(navController, userName) },
        containerColor = backgroundColor
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "welcome",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("welcome") {
                WelcomeScreen(
                    navController = navController,
                    userName = userName,
                    onUserNameChange = { viewModel.setUserName(it) },
                    isDarkMode = isDarkMode,
                    toggleDarkMode = { viewModel.setDarkMode(it) },
                    topBarColor = topBarColor
                )
            }
            composable("aboutMe") {
                AboutMeScreen(
                    userName = userName,
                    navController = navController,
                    isDarkMode = isDarkMode,
                    toggleDarkMode = { viewModel.setDarkMode(it) },
                    topBarColor = topBarColor
                )
            }
            composable("funFacts") {
                FunFactsScreen(
                    navController = navController,
                    isDarkMode = isDarkMode,
                    toggleDarkMode = { viewModel.setDarkMode(it) },
                    topBarColor = topBarColor
                )
            }
        }
    }
}
