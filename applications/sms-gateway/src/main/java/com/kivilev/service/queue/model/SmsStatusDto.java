package com.kivilev.service.queue.model;

public enum SmsStatusDto {
    ERROR(0),
    SUCCESFULL_SEND(1);

    private int id;

    SmsStatusDto(int id) {
        this.id = id;
    }
}
