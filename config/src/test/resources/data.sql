CREATE TABLE IF NOT EXISTS subscription (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(300),
    duration VARCHAR(10) NOT NULL CHECK (duration IN ('DAY', 'WEEK', 'MONTH', 'YEAR')),
    price DECIMAL(10,2) CHECK (price > 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
    );
-- Пример вставки с генерацией UUID через H2 функцию
INSERT INTO subscription (id, name, description, duration, price, created_at, updated_at)
VALUES (
           RANDOM_UUID(),
           'Monthly',
           'Monthly subscription',
           'MONTH',
           10.00,
           CURRENT_TIMESTAMP,
           CURRENT_TIMESTAMP
       );

INSERT INTO subscription (id, name, description, duration, price, created_at, updated_at)
VALUES (
           RANDOM_UUID(),
           'Yearly',
           'Yearly subscription',
           'YEAR',
           100.00,
           CURRENT_TIMESTAMP,
           CURRENT_TIMESTAMP
       );
