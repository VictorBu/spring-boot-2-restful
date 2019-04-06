create database if not exists tvseries
  character set 'utf8'
  collate 'utf8_general_ci';

create table if not exists tv_series (
   id int auto_increment,
   name varchar(50) not null,
   season_count int not null,
   origin_release date not null,
   status smallint not null default 0,
   delete_reason varchar(100) null,
   primary key (id)
);

create table if not exists tv_character (
    id int auto_increment,
    tv_series_id int not null,
    name varchar(50) not null,
    photo varchar(100) null,
    primary key (id)
);

insert into tv_series (name, season_count, origin_release, status, delete_reason)
  values ('West World', 2,'2016-10-02',1,'');