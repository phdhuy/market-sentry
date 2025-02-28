-- liquibase formatted sql

-- changeset duchuy:1740754170624-1
CREATE TABLE user_telegram
(
    id         UUID NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    deleted_at TIMESTAMP WITHOUT TIME ZONE,
    chat_id    VARCHAR(255),
    user_id    UUID,
    CONSTRAINT pk_user_telegram PRIMARY KEY (id)
);

-- changeset duchuy:1740754170624-2
ALTER TABLE user_telegram
    ADD CONSTRAINT FK_USER_TELEGRAM_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);


