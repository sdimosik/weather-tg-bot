package ru.spbstu.weather.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "weather_query", indexes = [Index(name = "idx_telegram_id", columnList = "telegramId")])
data class WeatherQuery(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val telegramId: Long, // ID пользователя Telegram
    val city: String, // Город, по которому запрашивалась погода
    val timestamp: LocalDateTime = LocalDateTime.now() // Время запроса
)
