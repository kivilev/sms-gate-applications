package com.kivilev.service;

import com.kivilev.exception.ClientNotFoundException;
import com.kivilev.service.model.Sms;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class SmsServiceImpl implements SmsService {
    private static final String SOURCE_ID = "web";
    private final Clock clock;
    private final CommonSmsService commonSmsService;
    private final ClientService clientService;

    public SmsServiceImpl(Clock clock, CommonSmsService commonSmsService, ClientService clientService) {
        this.clock = clock;
        this.commonSmsService = commonSmsService;
        this.clientService = clientService;
    }

    @Override
    public Sms sendNewSms(Sms sms) {
        if (!clientService.isClientExists(sms.getClientId())) {
            throw new ClientNotFoundException();
        }
        sms.setCreateDateTime(ZonedDateTime.now(clock));
        sms.setSourceId(SOURCE_ID);
        return commonSmsService.sendSms(sms);
    }

    @Override
    public List<Sms> getSmsMessages(Long clientId, int limit) {
        if (!clientService.isClientExists(clientId)) {
            throw new ClientNotFoundException();
        }
        return commonSmsService.getSmsMessages(clientId, limit);
    }
}
