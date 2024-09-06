package com.example.weatherapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.components.LoginScreen
import com.example.weatherapp.components.OnboardingScreen
import com.example.weatherapp.components.UserForm
import com.example.weatherapp.components.UserListScreen
import com.example.weatherapp.components.WeatherScreen
import com.example.weatherapp.dao.User
import com.example.weatherapp.ui.theme.WeatherAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val weatherViewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(navController, startDestination = "onboarding") {

                        composable("onboarding") {
                            OnboardingScreen { navController.navigate("login") }
                        }

                        composable("login") {
                            LoginScreen(context = this@MainActivity ) { navController.navigate("userList") }
                        }

                        composable("userList") {
                            UserListScreen(
                                viewModel = weatherViewModel,
                                onAddUser = { navController.navigate("userForm") },
                                onDeleteUser = { user -> weatherViewModel.deleteUser(user) },
                                navigateToWeather = { user ->
                                    navController.navigate("weather/${user.firstName}")
                                }
                            )
                        }
                        composable("userForm") {
                            UserForm(
                                onSaveUser = { user ->
                                    weatherViewModel.saveUser(user)
                                    navController.popBackStack()
                                },
                                onCancel = { navController.popBackStack() }
                            )
                        }

                        composable("weather/{userName}") { backStackEntry ->
                            val userName = backStackEntry.arguments?.getString("userName") ?: "Guest"
                            WeatherScreen(
                                weatherViewModel = weatherViewModel,
                                onLogout = {
                                    navController.navigate("login") {
                                        popUpTo("userList") { inclusive = true }
                                    }
                                },
                                onBack = { navController.popBackStack() },
                                userName = userName
                            )
                        }
                    }
                }
            }
        }
    }
}
