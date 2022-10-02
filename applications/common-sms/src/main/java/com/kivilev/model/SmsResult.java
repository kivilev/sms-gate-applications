package com.kivilev.model;

public enum SmsResult {
    NOT_SEND(0), SEND(1), ERROR(2);

    private final int id;

    SmsResult(int id) {
        this.id = id;
    }
}
