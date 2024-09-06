package com.example.weatherapp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.network.Resource
import com.example.weatherapp.dao.User
import com.example.weatherapp.respository.WeatherRepository
import com.example.weatherapp.util.WeatherState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _weatherState = MutableStateFlow<WeatherState>(WeatherState.Loading)
    val weatherState: StateFlow<WeatherState> = _weatherState

    private val _userList = MutableStateFlow<List<User>>(emptyList())
    val userList: StateFlow<List<User>> = _userList

    init {
        getUser()  // Fetch users on initialization
    }

    fun fetchWeatherData(lat: Double, lon: Double, units: String, appId: String) {
        viewModelScope.launch {
            _weatherState.value = WeatherState.Loading

            val result = repository.getWeatherData(lat, lon, units, appId)
            _weatherState.value = when (result) {
                is Resource.Success -> result.data?.let { WeatherState.Success(it) }!!
                is Resource.Error -> WeatherState.Error(result.message ?: "An error occurred")
                is Resource.Loading -> WeatherState.Loading
            }


        }
    }

    fun saveUser(user: User) {
        viewModelScope.launch {
            repository.insertUser(user)
            getUser()
        }
    }
    fun getUser() {
        viewModelScope.launch {
            val users = repository.getAllUsers()
            _userList.value = users

        }
    }
    fun deleteUser(user: User) {
        viewModelScope.launch {
            repository.deleteUser(user)
            val updatedList = _userList.value.filter { it.id != user.id }
            Log.d("WeatherViewModel", "Updated user list: $updatedList")
            _userList.value = updatedList
        }
    }
}
