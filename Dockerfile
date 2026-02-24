#FROM ubuntu:latest
#LABEL authors="Asus"
#
#ENTRYPOINT ["top", "-b"]

# Используем официальный образ Maven для сборки
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app

# Копируем файлы проекта
COPY pom.xml .
COPY src ./src

# Собираем приложение
RUN mvn clean package -DskipTests

# Второй этап - запуск
FROM openjdk:17-jdk-slim
WORKDIR /app

# Копируем собранный jar из первого этапа
COPY --from=build /app/target/coffeelab-loyalty-1.0.0.jar app.jar

# Порт приложения
EXPOSE 8080

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]