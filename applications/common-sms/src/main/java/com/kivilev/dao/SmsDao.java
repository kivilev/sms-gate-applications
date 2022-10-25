package com.kivilev.dao;

import com.kivilev.model.Sms;
import com.kivilev.model.SmsResult;
import com.kivilev.model.SmsState;

import java.util.List;
import java.util.Optional;

public interface SmsDao {
    Optional<Sms> getSms(Long clientId, String sourceId, String sourceIdempotencyKey);

    List<Sms> getSmsMessages(SmsState smsState, SmsResult smsResult, int packageSize);

    void saveSms(Sms sms);

    Optional<Sms> getSms(String smsId);

    List<Sms> getSmsMessages(Long clientId, int limit);
}
