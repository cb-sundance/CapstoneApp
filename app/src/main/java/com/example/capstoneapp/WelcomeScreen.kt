package com.example.capstoneapp

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

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
    var inputName by remember { mutableStateOf(userName) }
    var showError by remember { mutableStateOf(false) }
    var colorIndex by remember { mutableStateOf(0) }

    val lightColors = listOf(Color(0xFFFFF9C4), Color(0xFFFFCDD2), Color(0xFFC8E6C9))
    val darkColors = listOf(Color(0xFF1F1F1F), Color(0xFF2E2E2E), Color(0xFF3E3E3E))
    val currentColor by animateColorAsState(
        targetValue = if (isDarkMode) darkColors[colorIndex % darkColors.size]
        else lightColors[colorIndex % lightColors.size]
    )

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
            Image(
                painter = painterResource(id = R.drawable.welcome_image),
                contentDescription = "Welcome image",
                modifier = Modifier.size(150.dp).padding(bottom = 16.dp)
            )

            Text("Welcome!", fontSize = 30.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = inputName,
                onValueChange = {
                    inputName = it
                    showError = false
                    onUserNameChange(it)
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
                    if (inputName.isBlank()) showError = true
                    else navController.navigate("aboutMe")
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
