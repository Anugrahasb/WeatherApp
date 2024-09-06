package com.example.weatherapp.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.weatherapp.util.SharedPreferencesManager

@Composable
fun LoginScreen(context: Context, onLoginSuccess: () -> Unit) {
    val sharedPreferencesManager = remember { SharedPreferencesManager(context) }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    fun validateInputs(): Boolean {
        val savedUsername = sharedPreferencesManager.getUsername()
        val savedPassword = sharedPreferencesManager.getPassword()

        return when {
            username.isBlank() -> {
                errorMessage = "Username cannot be empty"
                false
            }

            password.isBlank() -> {
                errorMessage = "Password cannot be empty"
                false
            }

            username != savedUsername -> {
                errorMessage = "Invalid Username"
                false
            }

            password != savedPassword -> {
                errorMessage = "Invalid Password"
                false
            }

            else -> {
                errorMessage = ""
                true
            }
        }
    }
    //Save Credentials During Registration
    sharedPreferencesManager.saveCredentials("testapp@google.com", "Test@123456")

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .size(300.dp, 400.dp)
                .background(Color(0xFFADD8E6), shape = RoundedCornerShape(15.dp))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") }
                )
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    if (validateInputs()) {
                        onLoginSuccess()
                    }
                }) {
                    Text("Login")
                }
                if (errorMessage.isNotEmpty()) {
                    Text(text = errorMessage, color = Color.Red)
                }
            }
        }
    }
}
