-- apply alter tables
alter table stats_podiums modify positions varchar(4096) not null;
alter table stats_podiums add column title varchar(255);
alter table stats_podiums add column display varchar(255);
