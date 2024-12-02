package ru.spbstu.weather.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Service
import ru.spbstu.weather.models.CurrentWeatherResponse
import ru.spbstu.weather.models.GeocodingResponse

@Service
class WeatherService(restTemplateBuilder: RestTemplateBuilder) {

    private val restTemplate = restTemplateBuilder.build()

    @Value("\${openweather.api.key}")
    private lateinit var apiKey: String

    fun getCoordinates(city: String): Pair<Double, Double>? {
        val url = "http://api.openweathermap.org/geo/1.0/direct?q=$city&limit=1&appid=$apiKey"
        try {
            val response = restTemplate.getForObject(url, Array<GeocodingResponse>::class.java)
            return response?.firstOrNull()?.let { it.lat to it.lon }
        } catch (e: Exception) {
            println("Error fetching coordinates for city $city: ${e.message}")
            e.printStackTrace()
            return null
        }
    }

    fun getWeatherByCoordinates(lat: Double, lon: Double): String {
        val url = "https://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$lon&units=metric&appid=$apiKey"
        try {
            val response = restTemplate.getForObject(url, CurrentWeatherResponse::class.java)
            val description = response?.weather?.firstOrNull()?.description ?: "No description"
            val temperature = response?.main?.temp ?: "N/A"
            val pressure = response?.main?.pressure ?: "N/A"
            val humidity = response?.main?.humidity ?: "N/A"

            return """
                City: ${response?.name}
                Weather: $description
                Temperature: $temperature°C
                Pressure: $pressure hPa
                Humidity: $humidity%
            """.trimIndent()
        } catch (e: Exception) {
            println("Error fetching weather data: ${e.message}")
            e.printStackTrace()
            return "An error occurred while fetching weather data. Please try again later."
        }
    }
}

