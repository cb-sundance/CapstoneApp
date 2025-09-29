package com.cb.capstoneapp

import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
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
                WelcomeScreen { name, age, hobby ->
                    userName = name
                    userAge = age
                    userHobby = hobby
                    showNext = true
                }
            } else {
                NextScreen(userName, userAge, userHobby)
            }
        }
    }
}

@Composable
fun WelcomeScreen(onClickNext: (String, String, String) -> Unit) {
    // Track background color
    var bgColor by remember { mutableStateOf(Color(0xFFEDE7F6)) }
    val context = LocalContext.current // for Toast

    // Track user input
    var userName by remember { mutableStateOf("") }
    var userAge by remember { mutableStateOf("") }
    var userHobby by remember { mutableStateOf("") }

    // Track validation error
    var showError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome to My Capstone App",
            fontSize = 28.sp,
            color = Color(0xFF4A148C)
        )
        Spacer(modifier = Modifier.height(20.dp))

        // Name input
        TextField(
            value = userName,
            onValueChange = { userName = it },
            label = { Text("Enter your name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))

        // Age input
        TextField(
            value = userAge,
            onValueChange = { userAge = it },
            label = { Text("Enter your age") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))

        // Hobby input
        TextField(
            value = userHobby,
            onValueChange = { userHobby = it },
            label = { Text("Enter your hobby") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))

        if (showError) {
            Text(
                text = "Please fill out all fields!",
                color = Color.Red,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        // Button 1: Next Screen
        Button(
            onClick = {
                if (userName.isNotBlank() && userAge.isNotBlank() && userHobby.isNotBlank()) {
                    onClickNext(userName, userAge, userHobby)
                } else {
                    showError = true
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7B1FA2)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .height(60.dp)
                .width(200.dp)
        ) {
            Text(
                text = "Next Page",
                fontSize = 18.sp,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Button 2: Change Background + Toast feedback
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
                Toast.makeText(context, "Background changed!", Toast.LENGTH_SHORT).show()
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0097A7)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .height(60.dp)
                .width(200.dp)
        ) {
            Text(
                text = "Change Background Color",
                fontSize = 18.sp,
                color = Color.White
            )
        }
    }
}

@Composable
fun NextScreen(name: String, age: String, hobby: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD1C4E9))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome, $name!",
            fontSize = 26.sp,
            color = Color(0xFF311B92)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Age: $age",
            fontSize = 20.sp,
            color = Color(0xFF512DA8)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Hobby: $hobby",
            fontSize = 20.sp,
            color = Color(0xFF512DA8)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Thank you for checking out my Week 2 progress!",
            fontSize = 16,sp,
            color = Color(0xFF673AB7)
        )
    }
}
