package com.kivilev.service.processor;

import com.kivilev.service.model.SmsState;

public interface SmsStateProcessor {
    void process();

    SmsState getSmsState();
}
