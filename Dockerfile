# Базовый образ JDK для запуска Spring Boot приложения
FROM eclipse-temurin:17-jdk

# Указываем директорию внутри контейнера
WORKDIR /app

# Копируем скомпилированный .jar файл в контейнер
COPY ./build/libs/weather-0.0.1-SNAPSHOT.jar app.jar

# Указываем команду для запуска приложения
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
