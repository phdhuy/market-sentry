-- liquibase formatted sql

-- changeset duchuy:1721024726667-1
ALTER TABLE crypto DROP COLUMN explorer;

-- changeset duchuy:1721024726667-2
ALTER TABLE crypto
    ADD explorer VARCHAR(255);

