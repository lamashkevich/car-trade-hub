--liquibase formatted sql
--changeset lamashkevich:1
CREATE TABLE IF NOT EXISTS odometers (
    id BIGSERIAL PRIMARY KEY,
    value INTEGER NOT NULL,
    unit VARCHAR(10) NOT NULL,
    status VARCHAR(10) NOT NULL
);

--changeset lamashkevich:2
CREATE TABLE IF NOT EXISTS lots (
    id BIGSERIAL PRIMARY KEY,
    lot_number INTEGER UNIQUE NOT NULL,
    auction VARCHAR(10) NOT NULL,
    type VARCHAR(10) NOT NULL,
    make VARCHAR(50),
    model VARCHAR(50),
    year INTEGER,
    vin VARCHAR(50) NOT NULL,
    odometer_id BIGINT REFERENCES odometers(id),
    engine VARCHAR(20),
    fuel VARCHAR(10),
    transmission VARCHAR(10),
    drive VARCHAR(10),
    key BOOLEAN,
    damage VARCHAR(255),
    title VARCHAR(100),
    auction_date TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);