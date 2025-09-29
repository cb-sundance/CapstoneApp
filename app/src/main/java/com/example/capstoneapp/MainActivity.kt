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

            if (!showNext) {
                WelcomeScreen(
                    onClickNext = { name ->
                        userName = name
                        showNext = true
                    }
                )
            } else {
                NextScreen(userName)
            }
        }
    }
}

@Composable
fun WelcomeScreen(onClickNext: (String) -> Unit) {
    var bgColor by remember { mutableStateOf(Color(0xFFEDE7F6)) }
    val context = LocalContext.current
    var userName by remember { mutableStateOf("") }
    val colors = listOf(
        Color(0xFFEDE7F6),
        Color(0xFFC5CAE9),
        Color(0xFFB3E5FC),
        Color(0xFFFFF59D),
        Color(0xFFFFAB91)
    )
    var colorIndex by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome to My Capstone App",
            fontSize = 30.sp,
            color = Color(0xFF4A148C)
        )
        Spacer(modifier = Modifier.height(24.dp))

        TextField(
            value = userName,
            onValueChange = { userName = it },
            label = { Text("Enter your name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onClickNext(userName) },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7B1FA2)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth(0.6f)
        ) {
            Text(
                text = "Next Page",
                fontSize = 18.sp,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

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
                .fillMaxWidth(0.6f)
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
fun NextScreen(name: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD1C4E9))
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Welcome, $name!",
            fontSize = 26.sp,
            color = Color(0xFF311B92)
        )
        Spacer(modifier = Modifier.height(24.dp))

        // About Me section
        Text(
            text = "About Me",
            fontSize = 22.sp,
            color = Color(0xFF512DA8)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "This is my Capstone project where I’m learning Android development with Jetpack Compose. " +
                    "I enjoy coding, experimenting with UI layouts, and exploring new technologies. " +
                    "This app is a way to practice and demonstrate those skills while building something from scratch.",
            fontSize = 16.sp,
            color = Color(0xFF4A148C),
            lineHeight = 22.sp
        )
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Thank you for checking out my Week 2 progress!",
            fontSize = 16.sp,
            color = Color(0xFF673AB7)
        )
    }
}
