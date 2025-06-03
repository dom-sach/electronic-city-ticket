-- Enumy na podstawie klas enum w Java
CREATE TYPE role AS ENUM ('PASSENGER', 'TICKET_INSPECTOR', 'ADMINISTRATOR');
CREATE TYPE discount_type AS ENUM ('NORMAL', 'DISCOUNT');
CREATE TYPE ticket_category AS ENUM ('ONE_TIME', 'TIME', 'PERIOD');

-- Tabela użytkowników
CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       role role NOT NULL
);

-- Tabela tokenów (dla JWT)
CREATE TABLE token (
                       id SERIAL PRIMARY KEY,
                       token VARCHAR(255) UNIQUE,
                       revoked BOOLEAN NOT NULL,
                       expired BOOLEAN NOT NULL,
                       user_id INTEGER REFERENCES users(id) ON DELETE CASCADE
);

-- Tabela typów biletów
CREATE TABLE ticket_type (
                             id SERIAL PRIMARY KEY,
                             name VARCHAR(255) NOT NULL,
                             category ticket_category NOT NULL,
                             discount_type discount_type NOT NULL,
                             price NUMERIC(10, 2) NOT NULL,
                             duration_minutes INTEGER
);

-- Tabela pojazdów
CREATE TABLE vehicle (
                         id SERIAL PRIMARY KEY,
                         vehicle_id VARCHAR(100) NOT NULL
);

-- Tabela biletów
CREATE TABLE ticket (
                        id SERIAL PRIMARY KEY,
                        code VARCHAR(255) NOT NULL UNIQUE,
                        user_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
                        ticket_type_id INTEGER REFERENCES ticket_type(id),
                        purchase_date TIMESTAMP,
                        activation_date TIMESTAMP,
                        valid_until TIMESTAMP,
                        used BOOLEAN NOT NULL,
                        activated_in_id INTEGER REFERENCES vehicle(id)
);
