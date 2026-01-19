# Lab 10-12: HTTP Implementation with Java Spring Boot

## Student: Ivan Bilets

## Project Overview
Spring Boot REST API application with user management, database migrations, and security.

## Features
- REST API endpoints (GET/POST)
- SQLite database with Flyway migrations
- Spring Security with BCrypt password hashing
- 4-layer architecture (Controller-Service-Repository-Entity)
- Password protection (@JsonIgnore)

## API Endpoints
- GET /users/hello - Test endpoint
- GET /users - Get all users
- POST /users - Create user
- POST /users/login - Authenticate user

## How to Run
\\
./mvnw spring-boot:run
\\

## Technologies
- Java 17
- Spring Boot 4.0.0
- Spring Security
- Spring Data JPA
- SQLite
- Flyway
- Maven

## Submission
Lab 10 completed and ready for evaluation.


## Question
Что такое JPA?
Что такое Hibernate?
Hibernate — это просто перевод Java → SQL?
Где именно Hibernate превращает методы в SQL?
Почему мы не пишем SQL вручную?
Что такое Entity?
Как класс User становится таблицей в БД?
Что происходит при save(user)?
Кто парсит JSON в Java-объект?
Зачем нужны DTO?
Почему нельзя возвращать Entity напрямую?
Как Spring понимает, как из JSON сделать объект?
Что вообще происходит, когда я запускаю Spring Boot приложение?
Что такое Tomcat и зачем он нужен?
Что такое Spring Container и зачем он нужен?
Чем Spring Container отличается от Docker?
Как связаны между собой:
Controller → Service → Repository → Database
Когда я отправляю HTTP-запрос, по каким шагам он проходит внутри приложения?
Как Spring “прыгает” по коду по аннотациям?
