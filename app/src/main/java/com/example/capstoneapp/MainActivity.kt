package com.example.capstoneapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    private lateinit var dataStoreManager: DataStoreManager
    private val appViewModel: AppViewModel by viewModels { AppViewModelFactory(DataStoreManager(this)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataStoreManager = DataStoreManager(this)

        // Create notification channel right away
        NotificationUtils.createNotificationChannel(this)

        // schedule the periodic notification worker (once every 24 hours)
        val workRequest = PeriodicWorkRequestBuilder<SyncWorker>(24, TimeUnit.HOURS)
            .build()
        WorkManager.getInstance(applicationContext)
            .enqueueUniquePeriodicWork("capstone_notify_work", ExistingPeriodicWorkPolicy.KEEP, workRequest)

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

    val backgroundColor = if (isDarkMode) Color(0xFF121212) else Color(0xFFFFFFFF)
    val topBarColor = if (isDarkMode) Color(0xFF1F1F1F) else Color(0xFF1976D2)

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

// Bottom nav and helpers (same as your style)
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
    userName: String,
    onUserNameChange: (String) -> Unit,
    isDarkMode: Boolean,
    toggleDarkMode: (Boolean) -> Unit,
    topBarColor: Color
) {
    var localName by remember { mutableStateOf(userName) }
    var showError by remember { mutableStateOf(false) }
    var colorIndex by remember { mutableStateOf(0) }

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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = topBarColor, titleContentColor = Color.White),
                actions = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = if (isDarkMode) "Dark" else "Light", color = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = { toggleDarkMode(it) },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White)
                        )
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
            // Image (top-centered)
            Image(
                painter = painterResource(id = R.drawable.welcome_image),
                contentDescription = "Welcome image",
                modifier = Modifier.size(150.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))
            Text("Welcome!", fontSize = 30.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = localName,
                onValueChange = {
                    localName = it
                    onUserNameChange(it)
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
                    if (localName.isBlank()) showError = true else navController.navigate("aboutMe")
                },
                modifier = Modifier.fillMaxWidth(0.5f)
            ) { Text("Next Page") }

            Spacer(modifier = Modifier.height(12.dp))

            Button(onClick = { colorIndex++ }, modifier = Modifier.fillMaxWidth(0.5f)) {
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
    toggleDarkMode: (Boolean) -> Unit,
    topBarColor: Color
) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About Me") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = topBarColor, titleContentColor = Color.White),
                actions = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = if (isDarkMode) "Dark" else "Light", color = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Switch(checked = isDarkMode, onCheckedChange = { toggleDarkMode(it) }, colors = SwitchDefaults.colors(checkedThumbColor = Color.White))
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
            Image(painter = painterResource(id = R.drawable.about_image), contentDescription = "About image", modifier = Modifier.size(140.dp))

            Spacer(modifier = Modifier.height(12.dp))
            Text("About Me", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            Text("Hello, $userName!", fontSize = 22.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text("This is the About Me page.", fontSize = 18.sp)
            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = { NotificationUtils.showSimpleNotification(context, "Hello", "Have a great day, $userName!") }, modifier = Modifier.fillMaxWidth(0.5f)) {
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
    toggleDarkMode: (Boolean) -> Unit,
    topBarColor: Color
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Fun Facts") }, colors = TopAppBarDefaults.topAppBarColors(containerColor = topBarColor, titleContentColor = Color.White))
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
            Image(painter = painterResource(id = R.drawable.funfacts_image), contentDescription = "Fun Facts image", modifier = Modifier.size(140.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Text("Fun Facts", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(10.dp))
            Text("• I love gaming and the process behind making them!", fontSize = 17.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("• My favorite color is yellow, but not to wear", fontSize = 17.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("• I enjoy learning new tech.", fontSize = 17.sp)
        }
    }
}
