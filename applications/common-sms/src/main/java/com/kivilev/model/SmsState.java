package com.kivilev.model;

public enum SmsState {
    NEW_SMS(0),
    SENT_TO_SMS_GATE(1),
    SENT_TO_CLIENT(2);

    private final int id;

    SmsState(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
