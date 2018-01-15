/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2017/2/22 0:17:05                            */
/*==============================================================*/


drop table if exists lottery;


/*==============================================================*/
/* Table: lottery                                                */
/*==============================================================*/
create table lottery
(
   code                varchar(20) not null,
   numbers             varchar(20),
   lottery_date        varchar(20),
   update_time          datetime,
   primary key (code)
);

create table setting
(
   id                   int not null auto_increment,
   setting_name             varchar(20),
   setting_value        varchar(20),
   primary key (id)
);

