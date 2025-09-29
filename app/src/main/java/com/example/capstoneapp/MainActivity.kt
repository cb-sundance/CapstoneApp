package com.cb.capstoneapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var showNext by remember { mutableStateOf(false) }
            var userName by remember { mutableStateOf("") }
            var userAge by remember { mutableStateOf("") }
            var userHobby by remember { mutableStateOf("") }

            if (!showNext) {
                WelcomeScreen(
                    userName,
                    userAge,
                    userHobby,
                    onNameChange = { userName = it },
                    onAgeChange = { userAge = it },
                    onHobbyChange = { userHobby = it },
                    onClickNext = {
                        showNext = true
                    }
                )
            } else {
                NextScreen(userName, userAge, userHobby)
            }
        }
    }
}

@Composable
fun WelcomeScreen(
    userName: String,
    userAge: String,
    userHobby: String,
    onNameChange: (String) -> Unit,
    onAgeChange: (String) -> Unit,
    onHobbyChange: (String) -> Unit,
    onClickNext: () -> Unit
) {
    var bgColor by remember { mutableStateOf(Color(0xFFEDE7F6)) }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Welcome to My Capstone App", fontSize = 28.sp, color = Color(0xFF4A148C))
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = userName,
            onValueChange = onNameChange,
            label = { Text("Enter your name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = userAge,
            onValueChange = { input ->
                // Only allow digits
                if (input.all { it.isDigit() }) {
                    onAgeChange(input)
                }
            },
            label = { Text("Enter your age (numbers only)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = userHobby,
            onValueChange = onHobbyChange,
            label = { Text("Enter your hobby") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (errorMessage.isNotEmpty()) {
            Text(errorMessage, color = Color.Red, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                // Validation logic
                errorMessage = when {
                    userName.isBlank() -> "Please enter your name."
                    userAge.isBlank() -> "Please enter your age."
                    userAge.toIntOrNull() == null -> "Age must be a number."
                    userHobby.isBlank() -> "Please enter your hobby."
                    else -> {
                        onClickNext()
                        ""
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7B1FA2)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .height(60.dp)
                .width(200.dp)
        ) {
            Text("Next Page", fontSize = 18.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        val colors = listOf(
            Color(0xFFEDE7F6),
            Color(0xFFC5CAE9),
            Color(0xFFB3E5FC),
            Color(0xFFFFF59D),
            Color(0xFFFFAB91)
        )
        var colorIndex by remember { mutableStateOf(0) }

        Button(
            onClick = {
                colorIndex = (colorIndex + 1) % colors.size
                bgColor = colors[colorIndex]
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0097A7)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .height(60.dp)
                .width(200.dp)
        ) {
            Text("Change Background Color", fontSize = 18.sp, color = Color.White)
        }
    }
}

@Composable
fun NextScreen(userName: String, userAge: String = "", userHobby: String = "") {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD1C4E9))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Welcome, $userName!",
            fontSize = 28.sp,
            color = Color(0xFF311B92)
        )
        Spacer(modifier = Modifier.height(12.dp))

        if (userAge.isNotBlank()) {
            Text(
                "Age: $userAge",
                fontSize = 20.sp,
                color = Color(0xFF512DA8),
                lineHeight = 24.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (userHobby.isNotBlank()) {
            Text(
                "Hobby: $userHobby",
                fontSize = 20.sp,
                color = Color(0xFF512DA8),
                lineHeight = 24.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Text(
            "You can add more features here next week.",
            fontSize = 18.sp,
            color = Color(0xFF512DA8),
            lineHeight = 24.sp
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Thank you for checking out my Week 2 progress!",
            fontSize = 16.sp,
            color = Color(0xFF673AB7)
        )
    }
}

