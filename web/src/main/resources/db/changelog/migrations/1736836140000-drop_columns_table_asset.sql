-- liquibase formatted sql

-- changeset duchuy:1736836137823-6
ALTER TABLE asset DROP COLUMN change_percent24hr;
ALTER TABLE asset DROP COLUMN floor;
ALTER TABLE asset DROP COLUMN market_cap_usd;
ALTER TABLE asset DROP COLUMN max_supply;
ALTER TABLE asset DROP COLUMN name_vn;
ALTER TABLE asset DROP COLUMN rank;
ALTER TABLE asset DROP COLUMN supply;
ALTER TABLE asset DROP COLUMN volume_usd24hr;
ALTER TABLE asset DROP COLUMN vwap24hr;

