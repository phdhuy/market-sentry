-- liquibase formatted sql

-- changeset duchuy:1725987560959-1
ALTER TABLE asset
    ADD asset_type VARCHAR(255);
ALTER TABLE asset
    ADD floor VARCHAR(255);
ALTER TABLE asset
    ADD logo VARCHAR(255);
ALTER TABLE asset
    ADD name_vn VARCHAR(255);


-- changeset duchuy:1725987560959-10
DROP TABLE stock CASCADE;

