CREATE TABLE tasks
(
    id          UUID    NOT NULL,
    title       VARCHAR(255),
    description VARCHAR(255),
    status      VARCHAR(255),
    created_at  TIMESTAMP WITHOUT TIME ZONE,
    updated_at  TIMESTAMP WITHOUT TIME ZONE,
    deleted     BOOLEAN NOT NULL,
    CONSTRAINT pk_tasks PRIMARY KEY (id)
);