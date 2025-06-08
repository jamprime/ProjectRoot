CREATE TABLE event_store
(
    id           UUID NOT NULL,
    event_type   VARCHAR(31),
    aggregate_id UUID,
    timestamp    TIMESTAMP WITHOUT TIME ZONE,
    title        VARCHAR(255),
    description  VARCHAR(255),
    status       VARCHAR(255),
    CONSTRAINT pk_event_store PRIMARY KEY (id)
);