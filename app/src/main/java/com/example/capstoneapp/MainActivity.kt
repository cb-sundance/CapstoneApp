package com.example.capstoneapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
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
            modifier = Modifier.padding(paddingValues)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(
    navController: NavHostController,
    userName: MutableState<String>,
    isDarkMode: Boolean,
    topBarColor: Color,
    toggleDarkMode: (Boolean) -> Unit
) {
    var showError by remember { mutableStateOf(false) }
    var colorIndex by remember { mutableIntStateOf(0) }

    val colors = if (isDarkMode) {
        listOf(Color(0xFF1F1F1F), Color(0xFF2E2E2E), Color(0xFF3E3E3E))
    } else {
        listOf(Color(0xFFBBDEFB), Color(0xFFC8E6C9), Color(0xFFE91E63), Color(0xFFFFF9C4), Color(0xFFFFCDD2))
    }
    val currentColor by animateColorAsState(targetValue = colors[colorIndex % colors.size])

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Welcome Screen") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = topBarColor,
                    titleContentColor = Color.White
                ),
                actions = {
                    // Simple Switch for theme toggling (no missing icon dependency)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = if (isDarkMode) "Dark" else "Light", color = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = { toggleDarkMode(it) },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            )
        },
        containerColor = currentColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top-centered image for Welcome screen
            Image(
                painter = painterResource(id = R.drawable.welcome_image),
                contentDescription = "Welcome image",
                modifier = Modifier
                    .size(150.dp)
                    .padding(bottom = 16.dp)
            )

            Text("Welcome!", fontSize = 30.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = userName.value,
                onValueChange = {
                    userName.value = it
                    showError = false
                },
                label = { Text("Enter your name") },
                modifier = Modifier.fillMaxWidth(0.8f)
            )

            if (showError) {
                Text("Name cannot be empty!", color = Color.Red, fontSize = 14.sp, modifier = Modifier.padding(top = 5.dp))
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (userName.value.isBlank()) showError = true else navController.navigate("aboutMe")
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
fun AboutMeScreen(
    userName: String,
    navController: NavHostController,
    isDarkMode: Boolean,
    topBarColor: Color,
    toggleDarkMode: (Boolean) -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About Me") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = topBarColor,
                    titleContentColor = Color.White
                ),
                actions = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = if (isDarkMode) "Dark" else "Light", color = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = { toggleDarkMode(it) },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(if (isDarkMode) Color(0xFF121212) else Color(0xFFFAFAFA))
                .padding(paddingValues)
                .padding(20.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top-centered image for About Me
            Image(
                painter = painterResource(id = R.drawable.about_image),
                contentDescription = "About image",
                modifier = Modifier
                    .size(140.dp)
                    .padding(bottom = 16.dp)
            )

            Text("About Me", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(15.dp))
            Text("Hello, $userName!", fontSize = 22.sp)
            Spacer(modifier = Modifier.height(15.dp))
            Text("This is the About Me page.", fontSize = 18.sp)
            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = { Toast.makeText(context, "Have a great day, $userName!", Toast.LENGTH_SHORT).show() },
                modifier = Modifier.fillMaxWidth(0.5f)
            ) {
                Text("Greet Me")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FunFactsScreen(
    navController: NavHostController,
    isDarkMode: Boolean,
    topBarColor: Color,
    toggleDarkMode: (Boolean) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Fun Facts") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = topBarColor,
                    titleContentColor = Color.White
                ),
                actions = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = if (isDarkMode) "Dark" else "Light", color = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = { toggleDarkMode(it) },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(if (isDarkMode) Color(0xFF121212) else Color(0xFFE1F5FE))
                .padding(paddingValues)
                .padding(20.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top-centered image for Fun Facts
            Image(
                painter = painterResource(id = R.drawable.funfacts_image),
                contentDescription = "Fun Facts image",
                modifier = Modifier
                    .size(140.dp)
                    .padding(bottom = 16.dp)
            )

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
