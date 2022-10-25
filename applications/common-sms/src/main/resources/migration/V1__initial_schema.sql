create table sms
(
    sms_id                  bigserial    not null primary key,
    client_id               bigint       not null,
    source_id               varchar(100) not null,
    source_idempotency_key  varchar(100) not null,
    sms_text                text         not null,
    receiver_phone_number   varchar(20)  not null,
    create_date_time        varchar(30) not null, --timestamptz  not null,
    change_status_date_time varchar(30) --timestamptz
    /*
    cost                  numeric(5, 2)*/
);

alter table sms
    add constraint sms_idempotency_key_uq unique (source_idempotency_key, client_id, source_id);
create index sms_client_id_idx on sms (client_id);


create table sms_state_detail
(
    sms           bigint not null primary key,
    sms_state     varchar(200),
    sms_result    varchar(200),
    error_code    varchar(100),
    error_message varchar(1000)
);


/*
create table sms_cost
(
    country_code  varchar(10),
    operator_code varchar(20),
    cost          numeric(5, 2),
    primary key (country_code, operator_code)
);
*/
