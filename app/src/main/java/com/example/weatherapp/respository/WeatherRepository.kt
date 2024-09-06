package com.example.weatherapp.respository

import android.util.Log
import com.example.weatherapp.network.Resource
import com.example.weatherapp.network.WeatherApi
import com.example.weatherapp.dao.User
import com.example.weatherapp.dao.UserDao
import com.example.weatherapp.model.WeatherResponse
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val api: WeatherApi,
    private val userDao: UserDao
) {
    //api
    suspend fun getWeatherData(lat: Double, lon: Double, units: String, appId: String): Resource<WeatherResponse> {
        val response = try {
            api.getWeather(lat, lon, units, appId)
        } catch (e: Exception) {
            return Resource.Error("An unknown error occurred: ${e.message}")  // Handle the error case
        }
        return Resource.Success(response)
    }

    // Function to insert a user into the database
    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    // Function to get all users from the database
    suspend fun getAllUsers(): List<User> {
        return userDao.getAllUsers()
    }

    // Function to delete a user from the database
    suspend fun deleteUser(user: User) {
        userDao.deleteUser(user)
    }
}