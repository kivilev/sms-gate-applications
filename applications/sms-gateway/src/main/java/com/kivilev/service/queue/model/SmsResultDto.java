package com.kivilev.service.queue.model;

public enum SmsResultDto {
    ERROR(0),
    SUCCESFULL_SEND(1);

    private final int id;

    SmsResultDto(int id) {
        this.id = id;
    }
}
