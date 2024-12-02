package ru.spbstu.weather.service

import org.springframework.stereotype.Service
import ru.spbstu.weather.entity.WeatherQuery
import ru.spbstu.weather.repository.WeatherQueryRepository

@Service
class WeatherQueryService(private val weatherQueryRepository: WeatherQueryRepository) {
    fun saveQuery(telegramId: Long, city: String) {
        weatherQueryRepository.save(WeatherQuery(telegramId = telegramId, city = city))
    }

    fun getRecentQueries(telegramId: Long): List<String> {
        return weatherQueryRepository.findTop5ByTelegramIdOrderByTimestampDesc(telegramId)
            .map { it.city }
    }
}
