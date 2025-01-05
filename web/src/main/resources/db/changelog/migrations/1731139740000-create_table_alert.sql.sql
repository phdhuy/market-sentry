-- liquibase formatted sql

-- changeset duchuy:1735206650250-1
CREATE TABLE alert
(
    id                   UUID NOT NULL,
    created_at           TIMESTAMP WITHOUT TIME ZONE,
    updated_at           TIMESTAMP WITHOUT TIME ZONE,
    deleted_at           TIMESTAMP WITHOUT TIME ZONE,
    alert_type           VARCHAR(255),
    alert_condition_type VARCHAR(255),
    value                DOUBLE PRECISION,
    trigger_type         VARCHAR(255),
    expiration_at        TIMESTAMP WITHOUT TIME ZONE,
    alert_status         VARCHAR(255),
    asset_id             UUID,
    user_id              UUID,
    CONSTRAINT pk_alert PRIMARY KEY (id)
);

-- changeset duchuy:1735206650250-2
ALTER TABLE alert
    ADD CONSTRAINT FK_ALERT_ON_ASSET FOREIGN KEY (asset_id) REFERENCES asset (id);

-- changeset duchuy:1735206650250-3
ALTER TABLE alert
    ADD CONSTRAINT FK_ALERT_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

