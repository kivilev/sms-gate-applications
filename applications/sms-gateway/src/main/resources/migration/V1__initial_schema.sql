create table sms
(
    sms_id                  varchar(200) not null primary key,
    provider_external_id    varchar(200),
    sms_text                text         not null,
    receiver_phone_number   varchar(20)  not null,
    is_send_status_to_queue boolean
    /*create_dtime             timestamptz        not null,
    status                   smallint default 0 not null,
    send_dtime               timestamptz,
    last_change_status_dtime timestamptz,
    error_message         varchar(1000),
    error_code            varchar(100),
    cost                  numeric(5, 2)*/
);
create index provider_external_id_idx on sms (provider_external_id);

create table sms_state_detail
(
    sms           varchar(200) not null primary key,
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
    PRIMARY KEY (country_code, operator_code)
);
*/
