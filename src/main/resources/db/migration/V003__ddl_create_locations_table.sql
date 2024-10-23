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