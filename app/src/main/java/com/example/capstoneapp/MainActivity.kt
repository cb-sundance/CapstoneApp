package com.example.capstoneapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.*

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
    var userName by remember { mutableStateOf("") }
    var isDarkMode by remember { mutableStateOf(false) }

    val backgroundColor = if (isDarkMode) Color(0xFF121212) else Color(0xFFFFFFFF)
    val topBarColor = if (isDarkMode) Color(0xFF1F1F1F) else Color(0xFF1976D2)

    Scaffold(
        bottomBar = { BottomNavBar(navController, userName) },
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
                    onUserNameChange = { userName = it },
                    isDarkMode = isDarkMode,
                    toggleDarkMode = { isDarkMode = it },
                    topBarColor = topBarColor
                )
            }
            composable("aboutMe") {
                AboutMeScreen(
                    userName = userName,
                    navController = navController,
                    isDarkMode = isDarkMode,
                    toggleDarkMode = { isDarkMode = it },
                    topBarColor = topBarColor
                )
            }
            composable("funFacts") {
                FunFactsScreen(
                    navController = navController,
                    isDarkMode = isDarkMode,
                    toggleDarkMode = { isDarkMode = it },
                    topBarColor = topBarColor
                )
            }
        }
    }
}

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: @Composable () -> Unit
)

@Composable
fun BottomNavBar(navController: NavHostController, userName: String) {
    val items = listOf(
        BottomNavItem("welcome", "Welcome") { Icon(Icons.Filled.Home, contentDescription = null) },
        BottomNavItem("aboutMe", "About Me") { Icon(Icons.Filled.Info, contentDescription = null) },
        BottomNavItem("funFacts", "Fun Facts") { Icon(Icons.Filled.Face, contentDescription = null) }
    )

    NavigationBar(containerColor = Color(0xFF1976D2)) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        items.forEach { item ->
            NavigationBarItem(
                selected = currentDestination.isRouteActive(item.route),
                onClick = {
                    if (item.route == "aboutMe" && userName.isBlank()) return@NavigationBarItem
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
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

private fun NavDestination?.isRouteActive(route: String): Boolean {
    return this?.route == route
}
