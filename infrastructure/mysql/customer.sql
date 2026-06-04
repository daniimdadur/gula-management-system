use gula_customer_db;
show tables;

create table if not exists customer (
    id varchar(32) primary key,
    name varchar(255) not null,
    email varchar(255) not null unique,
    address text
);

alter table customer modify column id varchar(32) not null;

alter table customer rename to t_customer;

desc customer;