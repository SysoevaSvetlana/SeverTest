# Этап 1: Сборка приложения
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Копируем pom.xml и загружаем зависимости
COPY demo/pom.xml .
RUN mvn dependency:go-offline -B

# Копируем исходный код и собираем приложение
COPY demo/src ./src
RUN mvn clean package -DskipTests

# Этап 2: Создание финального образа
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Копируем собранный jar из этапа сборки
COPY --from=build /app/target/*.jar app.jar

# Открываем порт приложения
EXPOSE 8080

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]

