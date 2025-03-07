DROP TABLE IF EXISTS Users CASCADE;
DROP TABLE IF EXISTS Packages CASCADE;
DROP TABLE IF EXISTS Stats CASCADE;
DROP TABLE IF EXISTS Cards CASCADE;
DROP TABLE IF EXISTS Session CASCADE;
DROP TABLE IF EXISTS Decks CASCADE;
DROP TABLE IF EXISTS Trade CASCADE;

create table Users (
                       username varchar(255) primary key,
                       password varchar(255) not null,
                       coins int not null,
                       name varchar(255),
                       bio varchar(255),
                       image varchar(255)
);

create table Packages (
                          id serial primary key,
                          card_1 varchar(255) not null,
                          card_2 varchar(255) not null,
                          card_3 varchar(255) not null,
                          card_4 varchar(255) not null,
                          card_5 varchar(255) not null,
                          bought bool default false not null
);

create table Stats (
                       username varchar(255) not null,
                       games_played int default 0 not null,
                       games_won int default 0 not null,
                       games_lost int default 0 not null,
                       elo int default 100 not null,
                       FOREIGN KEY (username) references users(username)
);

create table Cards (
                       username varchar(255),
                       package_id int references packages(id),
                       card_id varchar(255) primary key not null,
                       name varchar(255) not null,
                       damage float not null,
                       monster_type boolean not null,
                       element_type varchar(50) not null,
                       enhancement varchar(50) not null,
                       locked boolean
);

create table Session (
                         username varchar(255) not null,
                         token varchar(255) not null,
                         FOREIGN KEY (username) references users(username)
);



create table Decks (
                       username varchar(255) not null unique,
                       card1_id varchar(255),
                       card2_id varchar(255),
                       card3_id varchar(255),
                       card4_id varchar(255)
);

create table Trade (
                       trade_id varchar(255) not null,
                       card_id varchar(255) not null,
                       type varchar(50) not null,
                       minDmg int,
                       element varchar(50)
);