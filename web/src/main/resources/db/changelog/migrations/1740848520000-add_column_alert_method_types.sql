-- liquibase formatted sql

-- changeset duchuy:1740848550839-1
ALTER TABLE alert
    ADD alert_method_types TEXT[];

