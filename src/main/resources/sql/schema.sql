create table if not exists record
(
    record_id   varchar,
    record_name varchar,
    create_time int8,
    primary key (record_id)
);
create table if not exists location
(
    lid varchar,
    x float8,
    y float8,
    primary key(lid)
);