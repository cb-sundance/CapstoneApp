package com.example.capstoneapp

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
    navController: NavHostController,
    userName: String,
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
                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = { toggleDarkMode(it) },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White)
                        )
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
            // Add your about image in drawable folder and name it "about_image"
            Image(
                painter = painterResource(id = R.drawable.about_image),
                contentDescription = "About image",
                modifier = Modifier.size(140.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))
            Text("About Me", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            Text("Hello, $userName!", fontSize = 22.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text("This is the About Me page.", fontSize = 18.sp)
            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { NotificationUtils.showSimpleNotification(context, "Hello", "Have a great day, $userName!") },
                modifier = Modifier.fillMaxWidth(0.5f)
            ) { Text("Greet Me") }
        }
    }
}
