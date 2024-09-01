-- liquibase formatted sql

-- changeset duchuy:1721022223101-1
ALTER TABLE portfolio
    ADD user_id UUID;

-- changeset duchuy:1721022223101-2
ALTER TABLE portfolio
    ADD CONSTRAINT FK_PORTFOLIO_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

