# Stellar Burgers — API Autotests (Диплом 2)

Автоматизированное тестирование REST API сервиса Stellar Burgers с использованием **RestAssured**, **JUnit 4** и **Allure**.

---

## Задание

Необходимо протестировать ключевые эндпоинты API:

### Пользователь (User)

**Создание пользователя:**
- Успешное создание уникального пользователя
- Создание уже существующего пользователя
- Создание пользователя с незаполненным обязательным полем

**Логин пользователя:**
- Успешный вход под существующим пользователем
- Вход с неверными данными (логин/пароль)

### Заказ (Order)

**Создание заказа:**
- Создание заказа с авторизацией
- Создание заказа без авторизации
- Создание заказа с ингредиентами
- Создание заказа без ингредиентов
- Создание заказа с неверным хешем ингредиента

---

## Стек

- **Java 11**
- **Maven**
- **RestAssured 5.5.0**
- **JUnit 4.13.2**
- **Gson**
- **Lombok**
- **Allure 2.21.0**

---

## Структура проекта
```bash
Diplom_2/
src/
└── test/
    └── java/
    │   ├── client/
    │   │   ├── AuthClient
    │   │   ├── BaseClient
    │   │   ├── OrderClient
    │   │   └── UserClient
    │   │
    │   ├── model/
    │   │   ├── request/
    │   │   │   ├── CreateOrderRequest
    │   │   │   ├── CreateUserRequest
    │   │   │   ├── LoginRequest
    │   │   │   ├── LogoutRequest
    │   │   │   ├── PasswordResetConfirmRequest
    │   │   │   ├── PasswordResetRequest
    │   │   │   └── UpdateUserRequest
    │   │   │
    │   │   └── response/
    │   │       ├── AuthResponse
    │   │       ├── BaseResponse
    │   │       ├── Order
    │   │       ├── OrderFull
    │   │       ├── OrderResponse
    │   │       ├── OrdersListResponse
    │   │       ├── User
    │   │       └── UserResponse
    │   │
    │   └── tests/
    │       ├── OrderTest
    │       ├── UserIncorrectTest
    │       ├── UserLoginTest
    │       └── UserTest
    └──resources
       └── allure.properties
```

---

## Как запустить проект

### 1. Клонирование репозитория
```bash
git clone https://github.com/berezikovM/Diplom_2.git
```
```bash
cd Diplom_2
```

### 2. Установка зависимостей
```bash
mvn clean install
```

### 3. Запуск тестов
```bash
mvn clean test
```

### 4. Генерация Allure отчета
```bash
mvn allure:serve
```
Все .html будут доступны по пути:
target/allure-results/