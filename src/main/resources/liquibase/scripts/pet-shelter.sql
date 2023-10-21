--liquibase formatted sql

--changeset botAdmin:1
create table if not exists users
(
    chat_id bigserial not null PRIMARY KEY,
    is_in_cat_shelter boolean not null,
    last_message text not null
);