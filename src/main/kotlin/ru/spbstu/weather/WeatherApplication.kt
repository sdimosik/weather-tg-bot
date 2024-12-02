package ru.spbstu.weather

import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import ru.spbstu.weather.service.WeatherBot

@SpringBootApplication
class WeatherApplication(private val weatherBot: WeatherBot) {
    @Bean
    fun runBot() = ApplicationRunner {
        weatherBot.startBot()
    }
}

fun main(args: Array<String>) {
    runApplication<WeatherApplication>(*args)
}
