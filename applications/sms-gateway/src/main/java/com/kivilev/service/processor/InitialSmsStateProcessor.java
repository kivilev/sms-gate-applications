package com.kivilev.service.processor;

import com.kivilev.service.model.Sms;

import java.util.List;

public interface InitialSmsStateProcessor {
    void process(List<Sms> smsMessages);
}
