-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Role table
CREATE TABLE role (
    role_id   UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL UNIQUE
);

-- User table
CREATE TABLE app_user (
    user_id     UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    username    VARCHAR(20) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    first_name  VARCHAR(20),
    last_name   VARCHAR(20),
    email       VARCHAR(255) NOT NULL UNIQUE,
    role_id     UUID NOT NULL,
    created_at  TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES role(role_id)
);

-- Category table
CREATE TABLE category (
    category_id   UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    category_name VARCHAR(50) NOT NULL,
    category_type VARCHAR(7) NOT NULL CHECK (category_type IN ('INCOME', 'EXPENSE')),
    user_id       UUID,
    CONSTRAINT fk_category_user FOREIGN KEY (user_id) REFERENCES app_user(user_id),
    CONSTRAINT uq_category_user UNIQUE (category_name, user_id)
);

-- Transaction table
CREATE TABLE transaction (
    transaction_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    user_id        UUID NOT NULL,
    category_id    UUID NOT NULL,
    amount         NUMERIC(15,2) NOT NULL CHECK (amount > 0),
    description    VARCHAR(255) NOT NULL,
    date           TIMESTAMP NOT NULL,
    active         BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_tx_user FOREIGN KEY (user_id) REFERENCES app_user(user_id),
    CONSTRAINT fk_tx_category FOREIGN KEY (category_id) REFERENCES category(category_id)
);

-- Indexes for performance
CREATE INDEX idx_tx_user_date ON transaction(user_id, date);
CREATE INDEX idx_tx_category ON transaction(category_id);
CREATE INDEX idx_tx_user_category ON transaction(user_id, category_id);
CREATE INDEX idx_user_email ON app_user(email);
CREATE INDEX idx_user_username ON app_user(username);