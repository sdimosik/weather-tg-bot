package ru.spbstu.weather.service

import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.entities.ChatId
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class WeatherBot(
    private val weatherService: WeatherService,
    private val userService: UserService,
    private val weatherQueryService: WeatherQueryService,
    @Value("\${telegram.bot.token}") private val token: String,
) {

    fun startBot() {
        val bot = bot {
            token = this@WeatherBot.token
            dispatch {
                command("weather") {
                    val chatId = update.message?.chat?.id ?: return@command
                    val username = update.message?.chat?.username
                    val city = getCityFromMessage(update.message?.text) ?: run {
                        bot.sendMessage(
                            ChatId.fromId(chatId),
                            "Please provide a city name, e.g., /weather London"
                        )
                        return@command
                    }

                    try {
                        val coordinates = weatherService.getCoordinates(city) ?: run {
                            bot.sendMessage(
                                ChatId.fromId(chatId),
                                "Could not find coordinates for $city. Please check the city name."
                            )
                            return@command
                        }

                        val (lat, lon) = coordinates
                        val weatherResponse = weatherService.getWeatherByCoordinates(lat, lon)

                        bot.sendMessage(ChatId.fromId(chatId), weatherResponse)
                        weatherQueryService.saveQuery(chatId, city)
                    } catch (e: Exception) {
                        bot.sendMessage(ChatId.fromId(chatId), "An unexpected error occurred.")
                        e.printStackTrace()
                    }
                }

                command("setfavorite") {
                    val chatId = update.message?.chat?.id ?: return@command
                    val username = update.message?.chat?.username
                    val city = getCityFromMessage(update.message?.text) ?: run {
                        bot.sendMessage(
                            ChatId.fromId(chatId),
                            "Please provide a city name, e.g., /setfavorite London"
                        )
                        return@command
                    }

                    userService.setFavoriteCity(chatId, city, username)
                    bot.sendMessage(ChatId.fromId(chatId), "Your favorite city is now set to $city.")
                }


                command("favorite") {
                    val chatId = update.message?.chat?.id ?: return@command
                    val city = userService.getFavoriteCity(chatId) ?: run {
                        bot.sendMessage(
                            ChatId.fromId(chatId),
                            "You haven't set a favorite city yet. Use /setfavorite <city> to set one."
                        )
                        return@command
                    }

                    try {
                        val coordinates = weatherService.getCoordinates(city) ?: run {
                            bot.sendMessage(
                                ChatId.fromId(chatId),
                                "Could not find coordinates for your favorite city ($city)."
                            )
                            return@command
                        }

                        val (lat, lon) = coordinates
                        val weatherResponse = weatherService.getWeatherByCoordinates(lat, lon)
                        bot.sendMessage(ChatId.fromId(chatId), weatherResponse)
                    } catch (e: Exception) {
                        bot.sendMessage(ChatId.fromId(chatId), "An unexpected error occurred.")
                        e.printStackTrace()
                    }
                }

                command("history") {
                    val chatId = update.message?.chat?.id ?: return@command
                    val history = weatherQueryService.getRecentQueries(chatId)

                    if (history.isEmpty()) {
                        bot.sendMessage(ChatId.fromId(chatId), "You have no recent queries.")
                    } else {
                        val response = history.joinToString("\n") { "- $it" }
                        bot.sendMessage(ChatId.fromId(chatId), "Your recent queries:\n$response")
                    }
                }

                command("help") {
                    val chatId = update.message?.chat?.id ?: return@command
                    bot.sendMessage(
                        ChatId.fromId(chatId),
                        """
                        Available commands:
                        /weather <city> - Get the current weather for a city
                        /setfavorite <city> - Set your favorite city
                        /favorite - Get the weather for your favorite city
                        /history - Show your last 5 weather queries
                        /help - Show this help message
                        """.trimIndent()
                    )
                }
            }
        }
        bot.startPolling()
    }

    private fun getCityFromMessage(text: String?): String? {
        return text?.split(" ")?.getOrNull(1)
    }

}
