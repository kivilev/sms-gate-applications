package com.kivilev.service.model;

public enum SmsResult {
    NOT_PROCESSED(0),
    ERROR(1),
    SUCCESSFUL_PROCESSED(2);

    private final int id;

    SmsResult(int id) {
        this.id = id;
    }
}
