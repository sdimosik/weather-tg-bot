package ru.spbstu.weather.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.spbstu.weather.entity.User

interface UserRepository : JpaRepository<User, Long>
