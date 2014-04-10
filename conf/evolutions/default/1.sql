# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table answer (
  id                        bigint not null,
  answertext                varchar(255),
  note                      integer,
  reaction_counter          integer,
  constraint pk_answer primary key (id))
;

create table questionlist (
  id                        bigint not null,
  user                      varchar(255),
  constraint pk_questionlist primary key (id))
;

create sequence answer_seq;

create sequence questionlist_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists answer;

drop table if exists questionlist;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists answer_seq;

drop sequence if exists questionlist_seq;

