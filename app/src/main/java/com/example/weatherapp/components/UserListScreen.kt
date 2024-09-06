package com.example.weatherapp.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherapp.WeatherViewModel
import com.example.weatherapp.dao.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(
    viewModel: WeatherViewModel = hiltViewModel(),
    onAddUser: () -> Unit,
    onDeleteUser: (User) -> Unit,
    navigateToWeather: (User) -> Unit
) {
    val users by viewModel.userList.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(
                    text = "User List",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,) },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xFFB3E5FC),
                    scrolledContainerColor = Color.Blue,
                    navigationIconContentColor = Color.White,
                    titleContentColor = Color.Black,
                    actionIconContentColor = Color.White
                ),
                actions = {}
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddUser) {
                Icon(Icons.Default.Add, contentDescription = "Add User")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            itemsIndexed(users) { index, user ->
                SwipeToDeleteUser(user = user, onDeleteUser = onDeleteUser, index = index, onClick = {
                    navigateToWeather(user)
                })
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDeleteUser(user: User, index: Int, onDeleteUser: (User) -> Unit, onClick: () -> Unit) {
    val state = rememberSwipeToDismissBoxState()
    LaunchedEffect(state.currentValue) {
        if (state.currentValue == SwipeToDismissBoxValue.EndToStart) {
            onDeleteUser(user)
            state.reset()
        }
    }
    SwipeToDismissBox(
        state = state,
        modifier = Modifier.padding(8.dp),
        enableDismissFromStartToEnd = true,
        enableDismissFromEndToStart = true,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Icon",
                    tint = Color.White
                )
            }
        },
        content = {
            UserCard(user, index, onClick)
        }
    )
}


@Composable
fun UserCard(user: User, index: Int, onClick: () -> Unit) {
    val backgroundColors = listOf(
        Color(0xFFB3E5FC), // Light Blue
        Color(0xFFC8E6C9), // Light Green
        Color(0xFFFFF9C4), // Light Yellow
        Color(0xFFFFAB91), // Light Orange
        Color(0xFFCE93D8)  // Light Purple
    )

    val backgroundColor = backgroundColors[index % backgroundColors.size]

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(8.dp)
            .clickable(onClick = {
                onClick()
            }
            )
            .shadow(8.dp, shape = RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.White, backgroundColor),
                        startY = 0f,
                        endY = 100f
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${user.firstName} ${user.lastName}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = user.email,
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        }
    }
}
