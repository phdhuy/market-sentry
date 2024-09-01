-- liquibase formatted sql

-- changeset duchuy:1721724376955-8
ALTER TABLE price_usd DROP CONSTRAINT fk_price_usd_on_crypto;

-- changeset duchuy:1721724376955-9
ALTER TABLE transaction DROP CONSTRAINT fk_transaction_on_crypto;

-- changeset duchuy:1721724376955-10
ALTER TABLE watch_list DROP CONSTRAINT fk_watch_list_on_crypto;

-- changeset duchuy:1721724376955-1
CREATE TABLE asset
(
    id                 UUID NOT NULL,
    created_at         TIMESTAMP WITHOUT TIME ZONE,
    updated_at         TIMESTAMP WITHOUT TIME ZONE,
    deleted_at         TIMESTAMP WITHOUT TIME ZONE,
    identity           VARCHAR(255),
    rank               BIGINT,
    symbol             VARCHAR(255),
    name               VARCHAR(255),
    supply             DOUBLE PRECISION,
    max_supply         DOUBLE PRECISION,
    market_cap_usd     DOUBLE PRECISION,
    volume_usd24hr     DOUBLE PRECISION,
    change_percent24hr DOUBLE PRECISION,
    vwap24hr           DOUBLE PRECISION,
    explorer           VARCHAR(255),
    CONSTRAINT pk_asset PRIMARY KEY (id)
);

-- changeset duchuy:1721724376955-2
ALTER TABLE price_usd
    ADD asset_id UUID;

-- changeset duchuy:1721724376955-3
ALTER TABLE transaction
    ADD asset_id UUID;

-- changeset duchuy:1721724376955-4
ALTER TABLE watch_list
    ADD asset_id UUID;

-- changeset duchuy:1721724376955-5
ALTER TABLE price_usd
    ADD CONSTRAINT FK_PRICE_USD_ON_ASSET FOREIGN KEY (asset_id) REFERENCES asset (id);

-- changeset duchuy:1721724376955-6
ALTER TABLE transaction
    ADD CONSTRAINT FK_TRANSACTION_ON_ASSET FOREIGN KEY (asset_id) REFERENCES asset (id);

-- changeset duchuy:1721724376955-7
ALTER TABLE watch_list
    ADD CONSTRAINT FK_WATCH_LIST_ON_ASSET FOREIGN KEY (asset_id) REFERENCES asset (id);

-- changeset duchuy:1721724376955-11
DROP TABLE crypto CASCADE;

-- changeset duchuy:1721724376955-17
ALTER TABLE price_usd DROP COLUMN crypto_id;

-- changeset duchuy:1721724376955-18
ALTER TABLE transaction DROP COLUMN crypto_id;

-- changeset duchuy:1721724376955-19
ALTER TABLE watch_list DROP COLUMN crypto_id;

