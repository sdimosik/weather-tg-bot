package ru.spbstu.weather.entity

import jakarta.persistence.*

@Entity
@Table(name = "users") // Имя таблицы изменено на "users"
data class User(
    @Id
    val telegramId: Long, // Уникальный ID пользователя Telegram
    var username: String? = null, // Никнейм пользователя
    var favoriteCity: String? = null // Избранный город
)
