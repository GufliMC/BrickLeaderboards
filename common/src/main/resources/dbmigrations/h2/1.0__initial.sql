-- apply changes
create table stats_podiums (
  id                            uuid not null,
  name                          varchar(255) not null,
  stats_key                     varchar(255) not null,
  positions                     varchar(255) not null,
  created_at                    timestamp not null,
  updated_at                    timestamp not null,
  constraint pk_stats_podiums primary key (id)
);

