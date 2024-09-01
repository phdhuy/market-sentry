-- liquibase formatted sql

-- changeset duchuy:1721021836893-1
CREATE TABLE crypto
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
    explorer           DOUBLE PRECISION,
    CONSTRAINT pk_crypto PRIMARY KEY (id)
);

-- changeset duchuy:1721021836893-2
CREATE TABLE portfolio
(
    id         UUID NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    deleted_at TIMESTAMP WITHOUT TIME ZONE,
    name       VARCHAR(255),
    avatar     VARCHAR(255),
    is_default BOOLEAN,
    CONSTRAINT pk_portfolio PRIMARY KEY (id)
);

-- changeset duchuy:1721021836893-3
CREATE TABLE price_usd
(
    id         UUID NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    deleted_at TIMESTAMP WITHOUT TIME ZONE,
    price_usd  DOUBLE PRECISION,
    crypto_id  UUID,
    CONSTRAINT pk_price_usd PRIMARY KEY (id)
);

-- changeset duchuy:1721021836893-4
CREATE TABLE transaction
(
    id             UUID NOT NULL,
    created_at     TIMESTAMP WITHOUT TIME ZONE,
    updated_at     TIMESTAMP WITHOUT TIME ZONE,
    deleted_at     TIMESTAMP WITHOUT TIME ZONE,
    type           VARCHAR(255),
    quantity       INTEGER,
    price_per_unit DOUBLE PRECISION,
    transaction_at TIMESTAMP WITHOUT TIME ZONE,
    fee            DOUBLE PRECISION,
    total          DOUBLE PRECISION,
    portfolio_id   UUID,
    CONSTRAINT pk_transaction PRIMARY KEY (id)
);

-- changeset duchuy:1721021836893-5
CREATE TABLE watch_list
(
    id         UUID NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    deleted_at TIMESTAMP WITHOUT TIME ZONE,
    user_id    UUID,
    crypto_id  UUID,
    CONSTRAINT pk_watch_list PRIMARY KEY (id)
);

-- changeset duchuy:1721021836893-6
ALTER TABLE price_usd
    ADD CONSTRAINT FK_PRICE_USD_ON_CRYPTO FOREIGN KEY (crypto_id) REFERENCES crypto (id);

-- changeset duchuy:1721021836893-7
ALTER TABLE transaction
    ADD CONSTRAINT FK_TRANSACTION_ON_PORTFOLIO FOREIGN KEY (portfolio_id) REFERENCES portfolio (id);

-- changeset duchuy:1721021836893-8
ALTER TABLE watch_list
    ADD CONSTRAINT FK_WATCH_LIST_ON_CRYPTO FOREIGN KEY (crypto_id) REFERENCES crypto (id);

-- changeset duchuy:1721021836893-9
ALTER TABLE watch_list
    ADD CONSTRAINT FK_WATCH_LIST_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

