--liquibase formatted sql

----changeset elbr:0
 create schema if not exists entities;

----changeset elbr:1

  create table if not exists entities.users (
  name varchar (255) not null,
  email varchar(255) primary key,
  password varchar (255) not null,
  role varchar (255) not null,
  boolean blocked not null
  );


----changeset elbr:2

  create table if not exists entities.transactions (
  id serial primary key,
  user_email varchar (255) references users(email) not null,
  transaction_type varchar (255) not null,
  amount numeric (15, 2),
  date_time timestamptz not null,
  description varchar (255) not null,
  category varchar (255) not null
  );

----changeset elbr:3
  create table if not exists entities.goals (
  id serial primary key,
  user_email varchar (255) references users(email) not null,
  target_amount numeric (15, 2),
  current_amount numeric (15, 2)
  );





