DO
$$
BEGIN
   IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'ecommerce_user') THEN
CREATE ROLE ecommerce_user LOGIN PASSWORD 'ecommerce_pass';
END IF;
END
$$;

DO
$$
BEGIN
   IF NOT EXISTS (SELECT FROM pg_database WHERE datname='ecommerce') THEN
      CREATE DATABASE ecommerce OWNER ecommerce_user;
END IF;
END
$$;

\connect ecommerce

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

ALTER SCHEMA public OWNER TO ecommerce_user;

CREATE TABLE IF NOT EXISTS users (
                                     id            UUID PRIMARY KEY,
                                     name          VARCHAR(100) NOT NULL,
    lastname      VARCHAR(100) NOT NULL,
    password      VARCHAR(255) NOT NULL,
    email         VARCHAR(255) NOT NULL,
    document      VARCHAR(50),
    phone_number  VARCHAR(50),
    city          VARCHAR(100),
    country       VARCHAR(100),
    role          VARCHAR(50)  NOT NULL,
    status        VARCHAR(50)  NOT NULL,
    created_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    deleted_at    TIMESTAMP WITH TIME ZONE,
                                cart          UUID
                                );

DO
$$
BEGIN
   IF NOT EXISTS (
     SELECT 1 FROM pg_indexes
     WHERE schemaname = 'public'
       AND indexname  = 'users_email_key'
   ) THEN
ALTER TABLE users ADD CONSTRAINT users_email_key UNIQUE (email);
END IF;
END
$$;

INSERT INTO users (
    id, name, lastname, password, email, document, phone_number,
    city, country, role, status, created_at, updated_at, cart
)
VALUES (
           'b628f99a-b32e-4964-8d29-8014a7c13aa2',
           'tefis',
           'Castro',
           '$2a$10$C2FAKKrh9FOD.wxZzp5TMuUNzQh/ywmWVkXaTJHZ/sB.lJT5t52Pm', -- hash bcrypt
           'tefis2@gmail.com',
           '2135151515',
           '21251251515',
           'Sogamoso',
           'Colombia',
           'ROLE_ADMIN',
           'ACTIVE',
           NOW(),
           NOW(),
           '257a08b3-6805-4e7c-80ce-fa2762d6b669'
       )
    ON CONFLICT (email) DO NOTHING;