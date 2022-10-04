package com.kivilev.service.processor;

import com.kivilev.dao.SmsDao;
import com.kivilev.service.model.Sms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DequeSmsMessagesSmsStateProcessor implements InitialSmsStateProcessor {

    private final SmsDao smsDao;
    private final Logger logger = LoggerFactory.getLogger(SendSmsToProviderSmsStateProcessor.class);


    public DequeSmsMessagesSmsStateProcessor(
            SmsDao smsDao) {
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
                logger.debug(String.format("got new message. smdId: %s", sms.getSmsId()));
            });

            smsDao.saveSmsMessages(newSmsList);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
