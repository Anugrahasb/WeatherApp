package com.example.weatherapp.util

import com.example.weatherapp.model.Current
import com.example.weatherapp.model.WeatherResponse

sealed class WeatherState {
    object Loading : WeatherState()
    data class Success(val data: WeatherResponse) : WeatherState()
    data class Error(val message: String) : WeatherState()
}