FROM amazoncorretto:17-alpine

WORKDIR /app
COPY build/libs/weather-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8181

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
