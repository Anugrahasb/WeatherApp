package com.example.weatherapp.di

import android.content.Context
import androidx.room.Room
import com.example.weatherapp.network.WeatherApi
import com.example.weatherapp.dao.AppDatabase
import com.example.weatherapp.dao.UserDao
import com.example.weatherapp.respository.WeatherRepository
import com.example.weatherapp.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideWeatherRepository(api: WeatherApi, userDao: UserDao): WeatherRepository {
        return WeatherRepository(api, userDao)
    }

    @Singleton
    @Provides
    fun providePokeApi(): WeatherApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(WeatherApi::class.java)
    }

    // Provide AppDatabase
    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    // Provide UserDao
    @Singleton
    @Provides
    fun provideUserDao(db: AppDatabase): UserDao {
        return db.userDao()
    }

}