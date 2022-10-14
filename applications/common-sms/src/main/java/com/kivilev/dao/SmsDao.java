package com.kivilev.dao;

import com.kivilev.model.Sms;
import com.kivilev.model.SmsResult;
import com.kivilev.model.SmsState;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface SmsDao {
    boolean isSmsExists(String source, String sourceIdempotencyKey);

    List<Sms> getSmsMessages(SmsState smsState, SmsResult smsResult, int packageSize);

    void saveSms(Sms sms);

    Optional<Sms> getSms(String smsId);
}
