-- liquibase formatted sql

-- changeset duchuy:1725279778554-1
ALTER TABLE stock DROP COLUMN highest_price;
ALTER TABLE stock DROP COLUMN lowest_price;
ALTER TABLE stock DROP COLUMN open_price;
ALTER TABLE stock DROP COLUMN volume;

-- changeset duchuy:1725279778554-2
ALTER TABLE stock
    ADD highest_price VARCHAR(255);

-- changeset duchuy:1725279778554-4
ALTER TABLE stock
    ADD lowest_price VARCHAR(255);

-- changeset duchuy:1725279778554-6
ALTER TABLE stock
    ADD open_price VARCHAR(255);

-- changeset duchuy:1725279778554-8
ALTER TABLE stock
    ADD volume VARCHAR(255);

