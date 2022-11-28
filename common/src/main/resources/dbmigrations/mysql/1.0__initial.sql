-- apply changes
create table stats_podiums (
  id                            varchar(40) not null,
  name                          varchar(255) not null,
  stats_key                     varchar(255) not null,
  positions                     varchar(255) not null,
  created_at                    datetime(6) not null,
  updated_at                    datetime(6) not null,
  constraint pk_stats_podiums primary key (id)
);

