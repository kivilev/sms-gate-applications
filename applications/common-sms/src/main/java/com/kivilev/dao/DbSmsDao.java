package com.kivilev.dao;

import com.kivilev.dao.repository.SmsRepository;
import com.kivilev.model.Sms;
import com.kivilev.model.SmsResult;
import com.kivilev.model.SmsState;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Primary
public class DbSmsDao implements SmsDao {

    private final SmsRepository smsRepository;

    public DbSmsDao(SmsRepository smsRepository) {
        this.smsRepository = smsRepository;
    }

    @Override
    public boolean isSmsExists(String sourceId, String sourceIdempotencyKey) {
        return smsRepository.findBySourceIdAndSourceIdempotencyKey(sourceId, sourceIdempotencyKey).isPresent();
    }

    @Override
    public List<Sms> getSmsMessages(SmsState smsState, SmsResult smsResult, int packageSize) {
        return smsRepository.findAllBySmsStateAndResult(smsState, smsResult, packageSize);
    }

    @Override
    public void saveSms(Sms sms) {
        smsRepository.save(sms);
    }

    @Override
    public Optional<Sms> getSms(String smsId) {
        return smsRepository.findById(smsId);
    }

}
