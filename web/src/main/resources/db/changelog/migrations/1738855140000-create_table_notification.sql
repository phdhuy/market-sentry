-- liquibase formatted sql

-- changeset duchuy:1738855227660-1
CREATE TABLE notification
(
    id         UUID NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    deleted_at TIMESTAMP WITHOUT TIME ZONE,
    is_read    BOOLEAN,
    content    VARCHAR(255),
    alert_id   UUID,
    user_id    UUID,
    CONSTRAINT pk_notification PRIMARY KEY (id)
);

-- changeset duchuy:1738855227660-2
ALTER TABLE notification
    ADD CONSTRAINT FK_NOTIFICATION_ON_ALERT FOREIGN KEY (alert_id) REFERENCES alert (id);

-- changeset duchuy:1738855227660-3
ALTER TABLE notification
    ADD CONSTRAINT FK_NOTIFICATION_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

