--liquibase formatted sql

--changeset botAdmin:1
create table if not exists users
(
    chat_id bigserial not null PRIMARY KEY,
    in_cat_shelter boolean,
    last_message text,
    active boolean
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
    score int,
    last_message text,
    animal_id bigint
);

--changeset botAdmin:4
create table if not exists volunteers
(
    chat_id bigserial not null PRIMARY KEY,
    name text,
    data text,
    active boolean,
    reminder_active boolean,
    last_message text,
    chat_id_to_search bigint,
    animal_id_to_search bigint,
    reviewed_report_id bigint
);

--changeset botAdmin:5
create table if not exists reports
(
    id bigserial not null PRIMARY KEY,
    chat_id bigint,
    text text,
    date timestamp,
    approved boolean,
    report_photo_id bigint,
    animal_id bigint
);

--changeset botAdmin:6
create table if not exists report_photos
(
    id bigserial not null PRIMARY KEY,
    file_id text not null,
    width int not null,
    height int not null
);