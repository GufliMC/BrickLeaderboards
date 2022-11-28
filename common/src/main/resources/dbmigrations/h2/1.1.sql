-- apply alter tables
alter table stats_podiums alter column positions varchar(4096);
alter table stats_podiums add column title varchar(255);
alter table stats_podiums add column display varchar(255);
