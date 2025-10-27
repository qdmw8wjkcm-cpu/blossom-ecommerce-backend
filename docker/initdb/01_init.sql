-- crea el usuario/role que tu app usa
CREATE ROLE ecommerce_user LOGIN PASSWORD 'ecommerce_pass';

-- crea la base y se la asigna
CREATE DATABASE ecommerce_db OWNER ecommerce_user;

-- (opcional) asegura ownership de schema public
ALTER DATABASE ecommerce_db OWNER TO ecommerce_user;
\connect ecommerce_db
ALTER SCHEMA public OWNER TO ecommerce_user;