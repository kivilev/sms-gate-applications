package com.kivilev.dao;

import com.kivilev.dao.repository.SmsRepository;
import com.kivilev.service.model.Sms;
import com.kivilev.service.model.SmsResult;
import com.kivilev.service.model.SmsState;
import com.kivilev.service.model.SmsStatusResultInfo;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Primary
public class DbSmsDao implements SmsDao {
    private final SmsRepository smsRepository;

    public DbSmsDao(SmsRepository smsRepository) {
        this.smsRepository = smsRepository;
    }

    @Override
    public void saveSms(Sms sms) {
        smsRepository.save(sms);
    }

    @Override
    public void saveSmsMessages(List<Sms> smsList) {
        smsRepository.saveAll(smsList);
    }

    @Override
    public void saveSmsStatusResultInfo(String smsId, SmsStatusResultInfo smsStatusResultInfo) {
        var smsOptional = smsRepository.findBySmsId(smsId);
        smsOptional.ifPresent(sms -> {
            sms.setSmsStatusInfo(smsStatusResultInfo);
            smsRepository.save(sms);
        });
    }

    @Override
    public List<Sms> getSmsMessages(SmsState smsState, SmsResult smsResult, int packageSize) {
        return smsRepository.findAllBySmsStateAndResult(smsState, smsResult, packageSize);
    }

    @Override
    public List<Sms> findBySmsIds(List<String> smsIds) {
        return (List<Sms>) smsRepository.findAllById(smsIds);
    }

    @Override
    public List<Sms> getSmsMessagesReadyForSendingQueue(int packageSize) {
        return smsRepository.findAllSmsReadyForSendingQueue(packageSize);
    }
}
