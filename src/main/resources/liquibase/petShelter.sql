--liquibase formatted sql

--changeset ivanchikov:1
CREATE TABLE IF NOT EXISTS petshelter_project
(
id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
change_id BIGINT NOT NULL,
message TEXT NOT NULL,
);