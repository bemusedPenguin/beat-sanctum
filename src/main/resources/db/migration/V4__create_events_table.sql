CREATE TABLE events (
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    created_by  BIGINT NOT NULL REFERENCES users(id),
    event_date  DATE NOT NULL,
    created_at  TIMESTAMP NOT NULL DEFAULT NOW()
);