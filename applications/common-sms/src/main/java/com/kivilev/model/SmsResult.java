package com.kivilev.model;

public enum SmsResult {
    ERROR(0),
    SUCCESSFUL_PROCESSED(1);

    private final int id;

    SmsResult(int id) {
        this.id = id;
    }
}
