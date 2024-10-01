-- liquibase formatted sql

-- changeset duchuy:1721840739931-1
ALTER TABLE price_usd DROP CONSTRAINT fk_price_usd_on_asset;

-- changeset duchuy:1721840739931-7
DROP TABLE price_usd CASCADE;

