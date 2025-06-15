1) ./gradlew clean
2) ./gradlew bootJar
3) docker build -t project-command -f project-command/Dockerfile . && docker build -t project-query -f project-query/Dockerfile .
4) docker-compose up -d

Command API доступен на порту 8081
Query API доступен на порту 8082
RabbitMQ Management доступен на порту 15672
PostgreSQL Command доступен на порту 5433
PostgreSQL Read доступен на порту 5434
Redis доступен на порту 6379

1. Контейнеризация (Docker) ✅
Проект полностью контейнеризирован
Имеет Dockerfile для каждого сервиса
Использует docker-compose для оркестрации всех сервисов
Включает контейнеры для всех необходимых компонентов (приложения, базы данных, Redis, RabbitMQ)
2. Кеширование (Redis) ✅
Redis интегрирован в проект
Настроен как кэш для Spring Boot приложений
Имеет конфигурацию RedisCacheManager
Используется в обоих сервисах (command и query)
3. Event-driven архитектура (RabbitMQ) ✅
RabbitMQ полностью интегрирован
Настроены очереди для обмена сообщениями
Реализована публикация и подписка на события
Используется для асинхронной коммуникации между сервисами
4. Observability ✅
Реализована через Spring Boot Actuator
Настроены health checks
Интегрирован Prometheus для метрик
Включены основные эндпоинты мониторинга
5. CQRS и Event Sourcing ✅
Четкое разделение на command и query сервисы
Реализован Event Store для хранения событий
Используется паттерн Event Sourcing (видно по структуре событий и их обработке)
Есть отдельный модуль project-events для общих событий
Реализована обработка событий в query сервисе
