--liquibase formatted sql

--changeset botAdmin:1
create table if not exists users
(
    chat_id bigserial not null PRIMARY KEY,
    is_in_cat_shelter boolean not null,
    last_message text not null
);

--changeset botAdmin:2
create table if not exists animals
(
    id bigserial not null PRIMARY KEY,
    name text not null,
    is_cat boolean not null,
    is_male boolean not null,
    age int not null,
    notes text not null
);

--changeset botAdmin:3
create table if not exists adopters
(
    chat_id bigserial not null PRIMARY KEY,
    name text not null,
    data text not null
);

--changeset botAdmin:4
create table if not exists volunteers
(
    id bigserial not null PRIMARY KEY,
    name text not null,
    data text not null
);