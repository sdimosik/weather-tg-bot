package ru.spbstu.weather.service

import org.springframework.stereotype.Service
import ru.spbstu.weather.entity.User
import ru.spbstu.weather.repository.UserRepository

@Service
class UserService(private val userRepository: UserRepository) {
    fun setFavoriteCity(telegramId: Long, city: String, username: String?) {
        val user = userRepository.findById(telegramId).orElseGet {
            userRepository.save(User(telegramId = telegramId, username = username))
        }

        if (user.username != username) {
            user.username = username
        }

        user.favoriteCity = city
        userRepository.save(user)
    }

    fun getFavoriteCity(telegramId: Long): String? {
        return userRepository.findById(telegramId).orElse(null)?.favoriteCity
    }
}
