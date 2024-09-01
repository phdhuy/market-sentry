-- liquibase formatted sql

-- changeset duchuy:1721028284324-1
ALTER TABLE transaction
    ADD crypto_id UUID;

-- changeset duchuy:1721028284324-2
ALTER TABLE transaction
    ADD CONSTRAINT FK_TRANSACTION_ON_CRYPTO FOREIGN KEY (crypto_id) REFERENCES crypto (id);

