package ru.spbstu.weather.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.spbstu.weather.entity.WeatherQuery

interface WeatherQueryRepository : JpaRepository<WeatherQuery, Long> {
    fun findTop5ByTelegramIdOrderByTimestampDesc(telegramId: Long): List<WeatherQuery>
}
