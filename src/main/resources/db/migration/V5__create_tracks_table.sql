CREATE TABLE tracks (
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    event_id        BIGINT NOT NULL REFERENCES events(id),
    uploaded_by     BIGINT NOT NULL REFERENCES users(id),
    title           VARCHAR(255) NOT NULL,
    original_filename VARCHAR(255) NOT NULL,
    file_key        VARCHAR(255) NOT NULL UNIQUE,
    mime_type       VARCHAR(50) NOT NULL,
    file_size       BIGINT NOT NULL,
    duration_seconds INTEGER,
    uploaded_at     TIMESTAMP NOT NULL DEFAULT NOW()
);