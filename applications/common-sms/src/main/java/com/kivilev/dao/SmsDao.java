package com.kivilev.dao;

import com.kivilev.model.Sms;
import com.kivilev.model.SmsState;
import com.kivilev.model.SmsStatusInfo;

import java.util.List;
import java.util.function.Predicate;

public interface SmsDao {
    boolean isExists(String source, String sourceIdempotencyKey);

    List<Sms> getSmsList(Predicate<Sms> filterPredicate, int packageSize);

    void saveSms(Sms sms);

    void setSmsStatus(String smsId, SmsStatusInfo smsState);
}
