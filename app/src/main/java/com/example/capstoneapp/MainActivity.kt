package com.example.capstoneapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp() {
    val navController = rememberNavController()
    val userName = remember { mutableStateOf("") }
    var isDarkMode by remember { mutableStateOf(false) }

    val backgroundColor = if (isDarkMode) Color(0xFF121212) else Color(0xFFFFFFFF)
    val topBarColor = if (isDarkMode) Color(0xFF1F1F1F) else Color(0xFF1976D2)

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
                    userName = userName,
                    isDarkMode = isDarkMode,
                    topBarColor = topBarColor,
                    toggleDarkMode = { isDarkMode = it }
                )
            }
            composable("aboutMe") {
                AboutMeScreen(
                    userName = userName.value,
                    navController = navController,
                    isDarkMode = isDarkMode,
                    topBarColor = topBarColor,
                    toggleDarkMode = { isDarkMode = it }
                )
            }
            composable("funFacts") {
                FunFactsScreen(
                    navController = navController,
                    isDarkMode = isDarkMode,
                    topBarColor = topBarColor,
                    toggleDarkMode = { isDarkMode = it }
                )
            }
        }
    }
}

@Composable
fun BottomNavBar(navController: androidx.navigation.NavHostController, userName: String) {
    val items = listOf(
        BottomNavItem("welcome", "Welcome") { androidx.compose.material3.Icon(Icons.Filled.Home, null) },
        BottomNavItem("aboutMe", "About Me") { androidx.compose.material3.Icon(Icons.Filled.Info, null) },
        BottomNavItem("funFacts", "Fun Facts") { androidx.compose.material3.Icon(Icons.Filled.Face, null) }
    )

    NavigationBar(containerColor = Color(0xFF1976D2)) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        items.forEach { item ->
            NavigationBarItem(
                selected = currentDestination?.route == item.route,
                onClick = {
                    if (item.route == "aboutMe" && userName.isBlank()) return@NavigationBarItem
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { item.icon() },
                label = { Text(item.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    selectedTextColor = Color.White,
                    unselectedIconColor = Color.White.copy(alpha = 0.7f),
                    unselectedTextColor = Color.White.copy(alpha = 0.7f),
                    indicatorColor = Color(0xFF0D47A1)
                )
            )
        }
    }
}

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: @Composable () -> Unit
)
