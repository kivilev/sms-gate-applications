package com.kivilev.service.model;

public enum SmsState {
    NEW_FROM_QUEUE(0),
    SENT_TO_PROVIDER(1),
    SENT_TO_CLIENT(2),
    SENT_STATUS_TO_QUEUE(3);

    private final int id;

    SmsState(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
