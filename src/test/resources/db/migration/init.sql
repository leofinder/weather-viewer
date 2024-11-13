CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE sessions (
    id UUID PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    expires_at TIMESTAMP NOT NULL
);

CREATE INDEX sessions_user_id_idx ON sessions (user_id);

CREATE INDEX sessions_expires_at_idx ON sessions (expires_at);

CREATE TABLE locations (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    user_id BIGINT NOT NULL REFERENCES users(id),
    latitude DECIMAL(7, 4) NOT NULL,
    longitude DECIMAL(7, 4) NOT NULL,
    country VARCHAR(100),
    state VARCHAR(100),
    CONSTRAINT user_location_unique_constraint UNIQUE (user_id, name, latitude, longitude)
);

CREATE INDEX locations_user_id_idx ON locations (user_id);