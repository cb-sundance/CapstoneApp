package com.example.capstoneapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
fun FunFactsScreen(
    navController: NavHostController,
    isDarkMode: Boolean,
    toggleDarkMode: (Boolean) -> Unit,
    topBarColor: Color
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Fun Facts") },
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
                            onCheckedChange = { toggleDarkMode(it) },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            )
        },
        containerColor = if (isDarkMode) Color(0xFF121212) else Color(0xFFE1F5FE)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(if (isDarkMode) Color(0xFF121212) else Color(0xFFE1F5FE))
                .padding(paddingValues)
                .padding(20.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.funfacts_image),
                contentDescription = "Fun Facts image",
                modifier = Modifier
                    .size(140.dp)
                    .padding(bottom = 16.dp)
            )

            Text("Fun Facts", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(15.dp))
            Text("• I love gaming and the process behind making them!", fontSize = 17.sp)
            Spacer(modifier = Modifier.height(10.dp))
            Text("• My favorite color is yellow, but not to wear", fontSize = 17.sp)
            Spacer(modifier = Modifier.height(10.dp))
            Text("• I enjoy learning new tech.", fontSize = 17.sp)
        }
    }
}
