# Сервисы системы

## ClientService
- Регистрация клиента (+50 бонусов)
- Поиск по телефону
- Получение истории бонусов

## OrderService
- Создание заказа
- Добавление позиций
- Списание бонусов (1=1 руб, ≤30%)
- Завершение заказа (+5% кэшбэк)

## MenuService
- Получение списка товаров
- Фильтрация по категориям

## API Endpoints
| Метод | URL | Описание |
|-------|-----|----------|
| POST | /api/clients/register | Регистрация |
| GET | /api/clients/by-phone/{phone} | Поиск клиента |
| POST | /api/orders | Создать заказ |
| POST | /api/orders/{id}/items | Добавить товар |
| POST | /api/orders/{id}/spend-bonus | Списать бонусы |
| POST | /api/orders/{id}/complete | Завершить заказ |