package com.example.capstoneapp

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

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
                            onCheckedChange = toggleDarkMode,
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
            Text("This is the About Me page. You can describe yourself here, your interests, or any other info you'd like to share.", fontSize = 18.sp)
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
