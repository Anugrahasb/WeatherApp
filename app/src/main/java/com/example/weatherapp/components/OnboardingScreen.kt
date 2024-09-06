package com.example.weatherapp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weatherapp.R
import com.example.weatherapp.ui.theme.WeatherAppTheme

@Composable
fun OnboardingScreen(onLoginClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE1F5FE)) // Light blue background
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header image
        val headerImage: Painter = painterResource(id = R.drawable.img)
        Image(
            painter = headerImage,
            contentDescription = "Onboarding Header",
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(8.dp))
                .padding(bottom = 24.dp)
        )

        // Welcome text
        Text(
            text = "Welcome to the App",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Button
        Button(
            onClick = onLoginClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0288D1)), // Button color
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .padding(horizontal = 32.dp)
        ) {
            Text(
                text = "Login",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewOnboardingScreen() {
    WeatherAppTheme {
        OnboardingScreen(onLoginClick = {})
    }
}
