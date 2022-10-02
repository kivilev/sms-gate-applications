create table sms
(
    sms_id                   varchar(200)       not null primary key,
    external_id              varchar(200),
    sms_text                 text               not null,
    receiver_phone_number    varchar(20)        not null,
    source_id                varchar(100)       not null,
    create_dtime             timestamptz        not null,
    status                   smallint default 0 not null,
    send_dtime               timestamptz,
    last_change_status_dtime timestamptz,
    error_message            varchar(1000),
    error_code               varchar(100),
    cost                     numeric(5, 2)
);
create index sms_external_id_idx on sms (external_id);


create table sms_cost
(
    country_code  varchar(10),
    operator_code varchar(20),
    cost          numeric(5, 2),
    PRIMARY KEY (country_code, operator_code)
);
