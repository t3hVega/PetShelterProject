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
    kind smallint not null,
    age int not null,
    notes text not null
);

--changeset botAdmin:3
create table if not exists adopters
(
    chat_id bigserial not null PRIMARY KEY,
    name text,
    data text,
    animal_id bigint
);

--changeset botAdmin:4
create table if not exists volunteers
(
    id bigserial not null PRIMARY KEY,
    name text not null,
    data text not null
);

--changeset botAdmin:5
create table if not exists reports
(
    id bigserial not null PRIMARY KEY,
    chat_id bigint not null,
    text text not null,
    report_photo_id bigint
);

--changeset botAdmin:6
create table if not exists report_photos
(
    id bigserial not null PRIMARY KEY,
    file_id text not null,
    file_unique_id text not null,
    width int not null,
    height int not null,
    file_size int not null,
    file_path text
);