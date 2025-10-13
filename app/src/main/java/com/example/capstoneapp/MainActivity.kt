package com.example.capstoneapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()
    val userName = remember { mutableStateOf("") }

    Scaffold(
        bottomBar = {
            BottomNavBar(navController, userName.value)
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "welcome",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("welcome") { WelcomeScreen(navController, userName) }
            composable("aboutMe") { AboutMeScreen(userName.value, navController) }
            composable("funFacts") { FunFactsScreen(navController) }
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

    NavigationBar(
        containerColor = Color(0xFF1976D2)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        items.forEach { item ->
            NavigationBarItem(
                selected = currentDestination.isRouteActive(item.route),
                onClick = {
                    // Prevent About Me navigation if username is empty
                    if (item.route == "aboutMe" && userName.isBlank()) return@NavigationBarItem

                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(navController: NavHostController, userName: MutableState<String>) {
    var name by remember { mutableStateOf(userName.value) }
    var showError by remember { mutableStateOf(false) }
    var colorIndex by remember { mutableIntStateOf(0) }
    val colors = listOf(
        Color(0xFFBBDEFB),
        Color(0xFFC8E6C9),
        Color(0xFFE91E63),
        Color(0xFFFFF9C4),
        Color(0xFFFFCDD2)
    )
    val currentColor by animateColorAsState(targetValue = colors[colorIndex % colors.size])

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Welcome Screen") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1976D2),
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(currentColor)
                .padding(paddingValues)
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Welcome!", fontSize = 30.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    userName.value = it
                    showError = false
                },
                label = { Text("Enter your name") },
                modifier = Modifier.fillMaxWidth(0.8f)
            )

            if (showError) {
                Text(
                    text = "Name cannot be empty!",
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 5.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (name.isBlank()) {
                        showError = true
                    } else {
                        userName.value = name
                        navController.navigate("aboutMe")
                    }
                },
                modifier = Modifier.fillMaxWidth(0.5f)
            ) {
                Text("Next Page")
            }

            Spacer(modifier = Modifier.height(15.dp))

            Button(
                onClick = { colorIndex++ },
                modifier = Modifier.fillMaxWidth(0.5f)
            ) {
                Text("Change Background")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutMeScreen(userName: String, navController: NavHostController) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About Me") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1976D2),
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFAFAFA))
                .padding(paddingValues)
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("About Me", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(15.dp))
            Text("Hello, $userName!", fontSize = 22.sp)
            Spacer(modifier = Modifier.height(15.dp))
            Text("This is the About Me page.", fontSize = 18.sp)
            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    Toast.makeText(context, "Have a great day, $userName!", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth(0.5f)
            ) {
                Text("Greet Me")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FunFactsScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Fun Facts") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1976D2),
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE1F5FE))
                .padding(paddingValues)
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Fun Facts", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(15.dp))
            Text("• I love gaming and the process behind making them!", fontSize = 17.sp)
            Spacer(modifier = Modifier.height(10.dp))
            Text("• My favorite color is yellow, but not to wear", fontSize = 17.sp)
            Spacer(modifier = Modifier.height(10.dp))
            Text("• I enjoy learning new tech.", fontSize = 17.sp)
        }
    }
}
