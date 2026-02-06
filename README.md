# Тестовое задание — Приёмка поставок и отчёты

Кейс:

Имеется 3 поставщика; каждый поставщик может поставлять 2 вида груш и 2 вида яблок. Поставщики заранее сообщают свои цены на виды продукции на определённый период поставок.

Задача:

1. Создать интерфейс приёмки поставок от поставщиков. В одной поставке от поставщика может быть несколько видов продукции.
2. Создать отчёт: за выбранный период показать поступление видов продукции по поставщикам с итогами по весу и стоимости.

Требования:

- Данные сохраняются в PostgreSQL; схема создаётся через Liquibase.
- Backend: Java 17+, Spring Boot 3, Spring Data JPA (Hibernate).
- API реализует приёмку поставок и отчёты; frontend необязателен.
- В исходнике присутствуют миграции Liquibase: `db/changelog/db.changelog-001-init.yaml`, `db/changelog-002-seed-products.yaml`, `db/changelog-003-warehouse-stocks.yaml`, `db/changelog-004-seed-users.yaml`. По умолчанию `db.changelog-master.yaml` подключает миграции 001..004.

Структура важных файлов:

- `demo/src/main/resources/db/changelog/` — миграции Liquibase.
- `demo/src/main/resources/application.properties` — конфигурация (по умолчанию использует PostgreSQL).
- Основные контроллеры API:
  - `AuthController` — `/auth/register`, `/auth/login`
  - `SupplyController` — `/api/supplies` (POST — создать поставку, GET — список поставок)
  - `ReportController` — `/api/reports/supplies?from=YYYY-MM-DD&to=YYYY-MM-DD` (GET — отчёт по поставкам)

Короткая инструкция по запуску

1) Подготовить PostgreSQL (локально или в контейнере). Создать БД `supply_chain` или указать другую через переменные окружения.

Пример с docker-compose (локально):

```powershell
# Запустить контейнер postgres
docker run --name supply_chain_db -e POSTGRES_PASSWORD=postgres -e POSTGRES_USER=postgres -e POSTGRES_DB=supply_chain -p 5432:5432 -d postgres:15
```

2) Запустить приложение:

```powershell
cd demo
.\mvnw.cmd spring-boot:run
```

или собрать jar и запустить:

```powershell
cd demo
.\mvnw.cmd package -DskipTests
java -jar target/demo-*.jar
```

3) Переменные окружения (опционально):

- `SPRING_DATASOURCE_URL` — JDBC URL, по умолчанию `jdbc:postgresql://localhost:5432/supply_chain`
- `SPRING_DATASOURCE_USERNAME` — по умолчанию `postgres`
- `SPRING_DATASOURCE_PASSWORD` — по умолчанию `postgres`
- `SPRING_LIQUIBASE_ENABLED` — включить/отключить Liquibase (`true`/`false`)

Миграции:

- По умолчанию применяется `db/changelog/db.changelog-master.yaml`, который подключает миграции 001..004 в указанном порядке:
  1. `001-init` — создание таблиц и FK
  2. `002-seed-products` — вставка двух яблок и двух груш
  3. `003-warehouse-stocks` — (существующая миграция проекта)
  4. `004-seed-users` — создание тестового покупателя и поставщиков

- В репозитории также есть `db/changelog/db.changelog-005-seed-users.yaml` (альтернативная/дополнительная миграция для пользователей). Если нужно применить 005 вместо/после 004, подключите её в `db.changelog-master.yaml`.

Аутентификация

- Эндпоинты защищены JWT; свободные: `/auth/**`, `/v3/api-docs/**`, `/swagger-ui/**`.
- Пример регистрации и логина (JSON):

Register (`POST /auth/register`):

```json
{
  "email": "buyer@example.com",
  "password": "password123",
  "role": "BUYER"
}
```

Login (`POST /auth/login`):

```json
{
  "email": "buyer@example.com",
  "password": "password123"
}
```

Ответ логина содержит JWT, который нужно передавать в заголовке `Authorization: Bearer <token>` для доступа к защищённым эндпоинтам.

Пример создания поставки

POST `/api/supplies` (Authorization required)

Пример тела (JSON):

```json
{
  "warehouseId": 1,
  "supplier": "SUPPLIER_A",
  "supplyDate": "2026-02-05",
  "items": [
    { "productId": 1, "weightKg": 100.5 },
    { "productId": 2, "weightKg": 50 }
  ]
}
```

Пример запроса отчёта

GET `/api/reports/supplies?from=2026-02-01&to=2026-02-28` (Authorization required)

Дополнительные заметки

- DTO и модели находятся в `com.example.demo.dto` и `com.example.demo.model`.
- Миграции и пример данных настроены для быстрого старта (после поднятия БД Liquibase создаст схемы и добавит продукты/пользователей).
- Если потребуется, могу добавить инструкции по запуску в Docker Compose или пример Postman коллекции.

Контакты и публикация

- Сборка и исходники готовы для заливки в GitHub/GitLab — весь проект находится в данной папке.

Если хочешь, добавлю: пример Docker Compose для базы + приложение, или автоматически подключу `db.changelog-005-seed-users.yaml` в мастер.
