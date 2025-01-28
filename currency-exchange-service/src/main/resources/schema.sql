CREATE TABLE currency_exchange (
    id BIGINT PRIMARY KEY,
    currency_from VARCHAR(3),
    currency_to VARCHAR(3),
    exchange_rate DECIMAL(19, 4)
);