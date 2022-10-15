package com.kivilev.service;

import com.kivilev.service.model.Sms;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CommonSmsServiceImpl implements CommonSmsService {
    @Override
    public Sms sendSms(Sms sms) {
        return sms;
    }

    @Override
    public List<Sms> getSmsMessages(Long clientId, Long limit) {
        return Collections.emptyList();
    }
}
