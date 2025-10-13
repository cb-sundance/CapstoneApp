package com.example.capstoneapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

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
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "Menu",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                NavigationDrawerItem(
                    label = { Text("Home") },
                    selected = false,
                    onClick = {
                        navController.navigate("welcome") {
                            popUpTo("welcome") { inclusive = true }
                        }
                        scope.launch { drawerState.close() }
                    }
                )

                NavigationDrawerItem(
                    label = { Text("About Me") },
                    selected = false,
                    onClick = {
                        navController.navigate("aboutMe/Guest")
                        scope.launch { drawerState.close() }
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Fun Facts") },
                    selected = false,
                    onClick = {
                        navController.navigate("funFacts")
                        scope.launch { drawerState.close() }
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Settings") },
                    selected = false,
                    onClick = {
                        Toast.makeText(LocalContext.current, "Settings clicked", Toast.LENGTH_SHORT).show()
                        scope.launch { drawerState.close() }
                    }
                )
            }
        }
    ) {
        MainAppContent(navController, drawerState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppContent(navController: NavHostController, drawerState: DrawerState) {
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Capstone App") },
                navigationIcon = {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                }
            )
        }
    ) { padding ->
        NavHost(
            navController,
            startDestination = "welcome",
            modifier = Modifier.padding(padding)
        ) {
            composable("welcome") { WelcomeScreen(navController) }
            composable("aboutMe/{userName}") { backStackEntry ->
                val name = backStackEntry.arguments?.getString("userName") ?: ""
                AboutMeScreen(name, navController)
            }
            composable("funFacts") { FunFactsScreen(navController) }
        }
    }
}

@Composable
fun WelcomeScreen(navController: NavHostController) {
    var name by remember { mutableStateOf("") }
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

        Button(
            onClick = {
                Toast.makeText(context, "Have a great day, $userName!", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth(0.5f)
        ) {
            Text("Greet Me")
        }

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            onClick = { navController.navigate("funFacts") },
            modifier = Modifier.fillMaxWidth(0.5f)
        ) {
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
        Text("• I love gaming and the process behind making them!", fontSize = 17.sp)
        Spacer(modifier = Modifier.height(10.dp))
        Text("• My favorite color is yellow, but not to wear", fontSize = 17.sp)
        Spacer(modifier = Modifier.height(10.dp))
        Text("• I enjoy learning new tech.", fontSize = 17.sp)
        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth(0.5f)
        ) {
            Text("Back")
        }
    }
}
