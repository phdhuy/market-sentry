-- liquibase formatted sql

-- changeset duchuy:1725279399043-1
CREATE TABLE stock
(
    id             UUID NOT NULL,
    created_at     TIMESTAMP WITHOUT TIME ZONE,
    updated_at     TIMESTAMP WITHOUT TIME ZONE,
    deleted_at     TIMESTAMP WITHOUT TIME ZONE,
    symbol         VARCHAR(255),
    title          VARCHAR(255),
    industry       VARCHAR(255),
    highest_price  DOUBLE PRECISION,
    lowest_price   DOUBLE PRECISION,
    open_price     DOUBLE PRECISION,
    volume         DOUBLE PRECISION,
    trading_status VARCHAR(255),
    CONSTRAINT pk_stock PRIMARY KEY (id)
);

