create table card_fact (
  id            serial not null,
  card_number   varchar(255),
  expire_date   timestamp,
  balance       numeric,
  user_id       int4,
  primary key (id)
);

create table usr (
  id              serial    not null,
  login           varchar(255) not null,
  primary key (id)
);

alter table if exists card_fact
  add constraint card_fact_user_fk foreign key (user_id) references usr;
