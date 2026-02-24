-- ==========================================================
-- CoffeeLab - Упрощенный SQL для H2 Database
-- ==========================================================

-- Очистка существующих данных (правильный порядок из-за FK)
DELETE FROM bonus_txn;
DELETE FROM order_item;
DELETE FROM orders;
DELETE FROM product;
DELETE FROM product_category;
DELETE FROM client;
DELETE FROM users;

-- Сброс последовательностей (H2 синтаксис)
ALTER TABLE users ALTER COLUMN id RESTART WITH 1;
ALTER TABLE client ALTER COLUMN id RESTART WITH 1;
ALTER TABLE product_category ALTER COLUMN id RESTART WITH 1;
ALTER TABLE product ALTER COLUMN id RESTART WITH 1;
ALTER TABLE orders ALTER COLUMN id RESTART WITH 1;
ALTER TABLE order_item ALTER COLUMN id RESTART WITH 1;
ALTER TABLE bonus_txn ALTER COLUMN id RESTART WITH 1;

-- ==========================================================
-- 1) БАЗОВЫЕ ТЕСТОВЫЕ ДАННЫЕ
-- ==========================================================

-- Пользователи (как в вашем коде)
INSERT INTO users (id, login, password_hash, role) VALUES
                                                       (1, 'barista1', 'hash', 'BARISTA'),
                                                       (2, 'admin1', 'hash', 'ADMIN');

-- Категории продуктов
INSERT INTO product_category (id, name) VALUES
                                            (1, 'Кофе'),
                                            (2, 'Чай'),
                                            (3, 'Десерты');

-- Продукты
INSERT INTO product (id, category_id, name, description, price, active) VALUES
                                                                            (1, 1, 'Капучино', 'Эспрессо + молоко', 200.00, true),
                                                                            (2, 1, 'Латте', 'Молоко + эспрессо', 220.00, true),
                                                                            (3, 2, 'Чай черный', 'Классический черный чай', 160.00, true),
                                                                            (4, 3, 'Чизкейк', 'Нью-Йорк чизкейк', 250.00, true);

-- ==========================================================
-- 2) ТЕСТОВЫЕ КЛИЕНТЫ И БОНУСЫ
-- ==========================================================

-- Создаем тестового клиента #1
INSERT INTO client (id, phone, full_name, bonus_balance, created_at)
VALUES (1, '+79991112233', 'Анна Смирнова', 150.00, CURRENT_TIMESTAMP());

-- Добавляем бонусную транзакцию (приветственный бонус)
INSERT INTO bonus_txn (id, client_id, order_id, txn_type, signed_amount, reason, created_at)
VALUES (1, 1, NULL, 'EARN', 150.00, 'Welcome bonus', CURRENT_TIMESTAMP());

-- Создаем завершенный заказ для истории
INSERT INTO orders (id, client_id, created_by_user_id, status, subtotal, bonus_spent, total, created_at, completed_at)
VALUES (1, 1, 1, 'COMPLETED', 500.00, 50.00, 450.00, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

-- Добавляем позиции в заказ
INSERT INTO order_item (id, order_id, product_id, product_name_snap, unit_price_snap, qty, line_total)
VALUES
    (1, 1, 1, 'Капучино', 200.00, 2, 400.00),
    (2, 1, 4, 'Чизкейк', 250.00, 1, 250.00);

-- Добавляем бонусные транзакции для заказа (списание и начисление)
INSERT INTO bonus_txn (id, client_id, order_id, txn_type, signed_amount, reason, created_at)
VALUES
    (2, 1, 1, 'SPEND', -50.00, 'Spend bonuses on order', CURRENT_TIMESTAMP()),
    (3, 1, 1, 'EARN', 22.50, 'Cashback 5% after completion', CURRENT_TIMESTAMP());

-- Обновляем баланс клиента с учетом всех операций (150 - 50 + 22.5 = 122.5)
UPDATE client SET bonus_balance = 122.50 WHERE id = 1;

-- ==========================================================
-- 3) ТЕКУЩИЙ (АКТИВНЫЙ) ЗАКАЗ ДЛЯ ТЕСТИРОВАНИЯ
-- ==========================================================

-- Создаем нового клиента для тестирования
INSERT INTO client (id, phone, full_name, bonus_balance, created_at)
VALUES (2, '+79995554433', 'Петр Иванов', 100.00, CURRENT_TIMESTAMP());

-- Добавляем приветственный бонус
INSERT INTO bonus_txn (id, client_id, order_id, txn_type, signed_amount, reason, created_at)
VALUES (4, 2, NULL, 'EARN', 100.00, 'Welcome bonus', CURRENT_TIMESTAMP());

-- Создаем новый заказ (статус NEW) для тестирования
INSERT INTO orders (id, client_id, created_by_user_id, status, subtotal, bonus_spent, total, created_at)
VALUES (2, 2, 1, 'NEW', 0.00, 0.00, 0.00, CURRENT_TIMESTAMP());

-- ==========================================================
-- 4) ПРОСТЫЕ SELECT-ЗАПРОСЫ ДЛЯ ПРОВЕРКИ (НЕ VIEW)
-- ==========================================================

-- Вместо создания VIEW, просто выводим данные для проверки
SELECT 'ТЕСТОВЫЕ ДАННЫЕ ЗАГРУЖЕНЫ:' as MESSAGE;

SELECT 'Клиенты:' as SECTION;
SELECT id, phone, full_name, bonus_balance FROM client;

SELECT 'Продукты:' as SECTION;
SELECT p.id, p.name, pc.name as category, p.price, p.active
FROM product p
         JOIN product_category pc ON pc.id = p.category_id;

SELECT 'Заказы:' as SECTION;
SELECT id, client_id, status, subtotal, bonus_spent, total, created_at
FROM orders;

SELECT 'Бонусные транзакции:' as SECTION;
SELECT bt.id, c.phone, bt.txn_type, bt.signed_amount, bt.reason, bt.created_at
FROM bonus_txn bt
         JOIN client c ON c.id = bt.client_id
ORDER BY bt.created_at DESC;

-- Фиксируем изменения
COMMIT;