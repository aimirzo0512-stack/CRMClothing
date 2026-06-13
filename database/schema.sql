-- Reference schema (Hibernate also auto-creates via ddl-auto=update).
-- Use this when bootstrapping a fresh PostgreSQL database manually.

CREATE TABLE IF NOT EXISTS roles (
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
    id        BIGSERIAL PRIMARY KEY,
    username  VARCHAR(100) UNIQUE NOT NULL,
    password  VARCHAR(255) NOT NULL,
    full_name VARCHAR(150)
);

CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role_id BIGINT NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

CREATE TABLE IF NOT EXISTS customers (
    id                BIGSERIAL PRIMARY KEY,
    full_name         VARCHAR(150) NOT NULL,
    email             VARCHAR(150) UNIQUE,
    phone             VARCHAR(50),
    address           VARCHAR(255),
    registration_date DATE DEFAULT CURRENT_DATE,
    loyalty_points    INTEGER DEFAULT 0
);

CREATE TABLE IF NOT EXISTS products (
    id             BIGSERIAL PRIMARY KEY,
    name           VARCHAR(150) NOT NULL,
    category       VARCHAR(80),
    size           VARCHAR(20),
    color          VARCHAR(40),
    price          NUMERIC(10,2) NOT NULL DEFAULT 0,
    stock_quantity INTEGER NOT NULL DEFAULT 0,
    description    VARCHAR(1000)
);

CREATE TABLE IF NOT EXISTS orders (
    id           BIGSERIAL PRIMARY KEY,
    customer_id  BIGINT REFERENCES customers(id),
    order_date   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status       VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    total_amount NUMERIC(10,2) NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS order_items (
    id         BIGSERIAL PRIMARY KEY,
    order_id   BIGINT NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    product_id BIGINT NOT NULL REFERENCES products(id),
    quantity   INTEGER NOT NULL,
    unit_price NUMERIC(10,2) NOT NULL
);
