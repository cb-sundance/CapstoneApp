package com.example.capstoneapp

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
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
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {

    // Permission launcher
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Permission granted
            } else {
                // Permission denied, handle accordingly
            }
        }


    private lateinit var dataStoreManager: DataStoreManager
    private lateinit var appViewModel: AppViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Request notification permission on Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }


        // Create notification channel
        NotificationUtils.createNotificationChannel(this)

        // Schedule daily WorkManager notifications
        val workRequest = PeriodicWorkRequestBuilder<SyncWorker>(24, TimeUnit.HOURS)
            .build()
        WorkManager.getInstance(applicationContext)
            .enqueueUniquePeriodicWork("capstone_notify_work", ExistingPeriodicWorkPolicy.KEEP, workRequest)

        // Show immediate welcome notification
        NotificationUtils.scheduleImmediateNotification(this)

        setContent {
            MyApp(appViewModel)
        }
    }
}

private fun NotificationUtils.scheduleImmediateNotification(mainActivity: MainActivity) {
    TODO("Not yet implemented")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp(viewModel: AppViewModel) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "welcome") {
        composable("welcome") { WelcomeScreen(navController) }
        composable("aboutMe/{userName}") { backStackEntry ->
            val name = backStackEntry.arguments?.getString("userName") ?: ""
            AboutMeScreen(name, navController)
        }
    }
}

@Composable
fun WelcomeScreen(navController: NavHostController) {
    var name by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var colorIndex by remember { mutableStateOf(0) }
    val colors = listOf(Color(0xFFBBDEFB), Color(0xFFC8E6C9), Color(0xFFFFF9C4), Color(0xFFFFCDD2))
    val currentColor by animateColorAsState(targetValue = colors[colorIndex % colors.size])

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

// ------------------- Bottom Navigation -------------------
data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: @Composable () -> Unit
)

@Composable
fun AboutMeScreen(userName: String, navController: NavHostController) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
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
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth(0.5f)
        ) {
            Text("Back")
        }

        Spacer(modifier = Modifier.height(15.dp))

        // New button for Commit 8
        Button(
            onClick = {
                Toast.makeText(context, "Have a great day, $userName!", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth(0.5f)
        ) {
            Text("Greet Me")
        }

        Spacer(modifier = Modifier.height(15.dp))

        // Button to navigate to Fun Facts with standard icon
        Button(
            onClick = { navController.navigate("funFacts") },
            modifier = Modifier.fillMaxWidth(0.5f)
        ) {
            Icon(Icons.Filled.Info, contentDescription = "Fun Icon")
            Spacer(modifier = Modifier.width(5.dp))
            Text("Fun Facts")
        }
    }
}

@Composable
fun FunFactsScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE1F5FE))
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Fun Facts", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(15.dp))
        Text("• I love gaming and the process behind making them!", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(10.dp))
        Text("• My favorite color is yellow, but not to wear", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(10.dp))
        Text("• I enjoy learning new tech.", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth(0.5f)
        ) {
            Text("Back")
        }
    }
}

private fun NavDestination?.isRouteActive(route: String): Boolean {
    return this?.route == route
}
