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

            if (!showNext) {
                WelcomeScreen { showNext = true }
            } else {
                NextScreen()
            }
        }
    }
}

@Composable
fun WelcomeScreen(onClickNext: () -> Unit) {
    // Track background color
    var bgColor by remember { mutableStateOf(Color(0xFFEDE7F6)) }
    val context = LocalContext.current // for Toast

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

        // Button 1: Next Screen
        Button(
            onClick = onClickNext,
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
        Button(
            onClick = {
                // Cycle through a few colors
                bgColor = when (bgColor) {
                    Color(0xFFEDE7F6) -> Color(0xFFC5CAE9)
                    Color(0xFFC5CAE9) -> Color(0xFFB3E5FC)
                    else -> Color(0xFFEDE7F6)
                }
                // show a short toast to give the user feedback
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
fun NextScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD1C4E9))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Next screen coming soon!",
            fontSize = 26.sp,
            color = Color(0xFF311B92)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "You can add more features here next week.",
            fontSize = 18.sp,
            color = Color(0xFF512DA8),
            lineHeight = 24.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Thank you for checking out my Week 1 progress!",
            fontSize = 16.sp,
            color = Color(0xFF673AB7)
        )
    }
}
