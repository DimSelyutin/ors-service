# Eureka Service

Сервис обнаружения сервисов (Service Discovery) для микросервисной архитектуры проекта Swimdom.

## Описание

Eureka Service предоставляет централизованный реестр для регистрации и обнаружения микросервисов в системе.

## Технологии

- Spring Boot 3.3.7
- Spring Cloud Netflix Eureka Server
- Java 17
- Maven

## Запуск

### Локальный запуск

1. Убедитесь, что у вас установлена Java 17
2. Выполните команду:
```bash
mvn spring-boot:run
```

### Запуск через Docker

1. Соберите проект:
```bash
mvn clean package
```

2. Запустите через Docker Compose:
```bash
docker-compose up -d
```

## Доступные эндпоинты

- **Eureka Dashboard**: http://localhost:8761
- **Health Check**: http://localhost:8761/actuator/health
- **Info**: http://localhost:8761/actuator/info
- **Metrics**: http://localhost:8761/actuator/metrics

## Конфигурация

Основные настройки находятся в файле `application.yml`:

- Порт: 8761
- Отключена саморегистрация (register-with-eureka: false)
- Отключено получение реестра (fetch-registry: false)
- Отключена самосохранность (enable-self-preservation: false)

## Интеграция с другими сервисами

Для подключения других сервисов к Eureka добавьте в их конфигурацию:

```yaml
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```
