package com.example.myapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
    NavHost(navController, startDestination = "welcome") {
        composable("welcome") { WelcomeScreen(navController) }
        composable("aboutMe/{userName}") { backStackEntry ->
            val name = backStackEntry.arguments?.getString("userName") ?: ""
            AboutMeScreen(userName = name)
        }
    }
}

@Composable
fun WelcomeScreen(navController: NavHostController) {
    var name by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var colorIndex by remember { mutableStateOf(0) }
    val colors = listOf(Color(0xFFBBDEFB), Color(0xFFC8E6C9), Color(0xFFFFF9C4), Color(0xFFFFCDD2))
    val currentColor = colors[colorIndex % colors.size]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(currentColor)
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
                    navController.navigate("aboutMe/$name")
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

@Composable
fun AboutMeScreen(userName: String) {
    var isDarkMode by remember { mutableStateOf(false) }
    val bgColor = if (isDarkMode) Color(0xFF212121) else Color(0xFFFAFAFA)
    val textColor = if (isDarkMode) Color.White else Color.Black

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("About Me", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = textColor)
        Spacer(modifier = Modifier.height(15.dp))
        Text("Hello, $userName!", fontSize = 22.sp, color = textColor)
        Spacer(modifier = Modifier.height(15.dp))
        Text("This is the About Me page.", fontSize = 18.sp, color = textColor)
        Spacer(modifier = Modifier.height(20.dp))

        // Settings / Dark Mode toggle
        Button(
            onClick = { isDarkMode = !isDarkMode },
            modifier = Modifier.fillMaxWidth(0.5f)
        ) {
            Text(if (isDarkMode) "Switch to Light Mode" else "Switch to Dark Mode")
        }
    }
}
