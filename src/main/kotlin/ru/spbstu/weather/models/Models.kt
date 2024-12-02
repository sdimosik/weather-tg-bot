package ru.spbstu.weather.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class GeocodingResponse(
    val name: String,
    val lat: Double,
    val lon: Double,
    val country: String
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class WeatherDescription(
    val description: String
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class CurrentWeatherResponse(
    val weather: List<WeatherDescription>, // Список описаний погоды (ясно, дождь и т.д.)
    val main: MainWeatherData,             // Основные данные о погоде (температура, влажность)
    val name: String                       // Название города
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class MainWeatherData(
    val temp: Double,       // Температура
    val pressure: Int,      // Давление
    val humidity: Int       // Влажность
)
