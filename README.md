# Структура 
- discovery-server - Eureka Server - сервер на котором запускаются все клиенты
- api-gateway - для маршрутизации к API
- order-service - сервис формирования заказов, клиент отправляет запрос, далее order-service направляет через WebClient запрос в inventory-service с целью проверить
  наличие товаров на складе, далее order-service получается ответ, если товар на складе есть, заказ формируется, если товара на складе нету, заказ не формируется
- inventory-service - сервис в котором хранится информация о количестве товара на складе
- notification-service - сервис уведомлений, сюда поступает сообщение из order-service через kafka,
  далее в notification-service формируется сообщение и направляется клиенту уведомление по почте, с помощью API Яндекс почты
- product-service - сервис товаров

# Используемые технологии
Основной проект:
- Java 17
- Maven
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- Spring Cloud
- Swagger
- Lombok
- PostgreSQL
  



