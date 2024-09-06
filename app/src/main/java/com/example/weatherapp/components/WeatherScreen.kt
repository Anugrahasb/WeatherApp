package com.example.weatherapp.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.weatherapp.R
import com.example.weatherapp.WeatherViewModel
import com.example.weatherapp.components.weather.Animation
import com.example.weatherapp.components.weather.ForecastComponent
import com.example.weatherapp.components.weather.HourlyComponent
import com.example.weatherapp.components.weather.WeatherComponent
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.util.WeatherState
import com.example.weatherapp.util.getFormattedDate
import java.time.LocalDate
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    weatherViewModel: WeatherViewModel = hiltViewModel(),
    onLogout: () -> Unit,
    onBack: () -> Unit,
    userName: String
) {
    val weatherState by weatherViewModel.weatherState.collectAsState()

    LaunchedEffect(Unit) {
        weatherViewModel.fetchWeatherData(
            appId = "b143bb707b2ee117e62649b358207d3e",
            lon = 77.65197822993314,
            lat = 12.9082847623315,
            units = "imperial"
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Weather") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.Black)
                    }
                },
                colors = TopAppBarColors(
                    containerColor = Color(0xFFB3E5FC),
                    scrolledContainerColor = Color.Blue,
                    navigationIconContentColor = Color.White,
                    titleContentColor = Color.Black,
                    actionIconContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Filled.ExitToApp, contentDescription = "Logout", tint = Color.Black)
                    }
                }
            )
        }
    ) { padding ->
        GradientBackground {
            when (weatherState) {
                is WeatherState.Loading -> {
                    Animation(
                        modifier = Modifier.fillMaxSize(),
                        animation = R.raw.animation_loading
                    )
                }

                is WeatherState.Success -> {
                    val data = (weatherState as WeatherState.Success).data
                    WeatherSuccessState(modifier = Modifier, response = data,userName = userName)
                }

                is WeatherState.Error -> {
                    WeatherErrorState(uiState = weatherState, viewModel = weatherViewModel)
                }
            }
        }
    }
}

@Composable
fun GradientBackground(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF81D4FA), Color(0xFF0288D1))
                )
            )
    ) {
        content()
    }
}

@Composable
private fun WeatherErrorState(
    modifier: Modifier = Modifier,
    uiState: WeatherState,
    viewModel: WeatherViewModel?,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Animation(
            modifier = Modifier
                .fillMaxWidth()
                .weight(8f),
            animation = R.raw.animation_error,
        )

        Button(onClick = { viewModel?.fetchWeatherData(
            appId = "b143bb707b2ee117e62649b358207d3e",
            lon = 77.65197822993314,
            lat = 12.9082847623315,
            units = "imperial"
        ) }) {
            Icon(
                imageVector = Icons.Filled.Refresh,
                contentDescription = "Retry",
            )
            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                style = MaterialTheme.typography.titleMedium,
                text = stringResource(R.string.retry),
                fontWeight = FontWeight.Bold,
            )
        }

        Text(
            modifier = modifier
                .weight(2f)
                .alpha(0.5f)
                .padding(horizontal = 16.dp, vertical = 16.dp),
            text = "Error: ${(uiState as WeatherState.Error).message}",
            textAlign = TextAlign.Center,
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun WeatherSuccessState(
    modifier: Modifier,
    response: WeatherResponse,
    userName: String,
) {
    val formattedDate = getFormattedDate()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.padding(top = 12.dp),
            text = userName,
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = formattedDate,
            style = MaterialTheme.typography.bodyLarge
        )

        AsyncImage(
            modifier = Modifier.size(64.dp),
            model = stringResource(
                R.string.icon_image_url,
                response.current?.weather?.get(0)?.icon.orEmpty(),
            ),
            contentScale = ContentScale.FillBounds,
            contentDescription = null,
            error = painterResource(id = R.drawable.ic_placeholder),
            placeholder = painterResource(id = R.drawable.ic_placeholder),
        )
        Text(
            text = stringResource(
                R.string.temperature_value_in_celsius,
                response.current.temp.toString()
            ),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
        )
        Text(
            modifier = Modifier.padding(start = 12.dp, end = 12.dp),
            text = response.current.weather[0].description,
            style = MaterialTheme.typography.bodyMedium,
        )
        Text(
            modifier = Modifier.padding(bottom = 4.dp),
            text = stringResource(
                R.string.feels_like_temperature_in_celsius,
                response.current.feels_like.toString()
            ),
            style = MaterialTheme.typography.bodySmall
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(painter = painterResource(id = R.drawable.ic_placeholder), contentDescription = null)
            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = response.current.sunrise.toString().lowercase(Locale.US).orEmpty(),
                style = MaterialTheme.typography.bodySmall,
            )
            Spacer(modifier = Modifier.width(16.dp))
            Image(painter = painterResource(id = R.drawable.ic_sunset), contentDescription = null)
            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = response.current.sunset.toString().lowercase(Locale.US).orEmpty(),
                style = MaterialTheme.typography.bodySmall,
            )
        }
        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
        ) {
            WeatherComponent(
                modifier = Modifier.weight(1f),
                weatherLabel = stringResource(R.string.wind_speed_label),
                weatherValue = response.current.wind_speed.toString(),
                weatherUnit = stringResource(R.string.wind_speed_unit),
                iconId = R.drawable.img_1,
            )
            WeatherComponent(
                modifier = Modifier.weight(1f),
                weatherLabel = stringResource(R.string.uv_index_label),
                weatherValue = response.current.uvi.toString(),
                weatherUnit = stringResource(R.string.uv_unit),
                iconId = R.drawable.img,
            )
            WeatherComponent(
                modifier = Modifier.weight(1f),
                weatherLabel = stringResource(R.string.humidity_label),
                weatherValue = response.current.humidity.toString(),
                weatherUnit = stringResource(R.string.humidity_unit),
                iconId = R.drawable.ic_humidity,
            )
        }
        Spacer(Modifier.height(16.dp))
    }
}