package com.kivilev.service.processor;

import com.kivilev.dao.SmsDao;
import com.kivilev.exception.ProcessingException;
import com.kivilev.service.model.Sms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DequeSmsMessagesSmsStateProcessor implements InitialSmsStateProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(DequeSmsMessagesSmsStateProcessor.class);
    private final SmsDao smsDao;

    public DequeSmsMessagesSmsStateProcessor(SmsDao smsDao) {
        this.smsDao = smsDao;
    }

    @Transactional
    @Override
    public void process(List<Sms> smsMessages) {
        try {
            var existedSmsList = smsDao.findBySmsIds(smsMessages.stream()
                    .map(Sms::getSmsId)
                    .toList());

            var newSmsList = smsMessages.stream()
                    .filter(sms -> !existedSmsList.contains(sms))
                    .collect(Collectors.toList());

            newSmsList.forEach(sms -> {
                LOGGER.debug(String.format("got new message. smdId: %s", sms.getSmsId()));
            });

            smsDao.saveSmsMessages(newSmsList);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            throw new ProcessingException(e.getMessage());
        }
    }
}
