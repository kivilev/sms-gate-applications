package com.kivilev.dao;

import com.kivilev.service.model.Sms;
import com.kivilev.service.model.SmsStatusResultInfo;

import java.util.List;
import java.util.function.Predicate;

public interface SmsDao {

    boolean isExists(String smsId);

    void saveSms(Sms sms);

    void saveSmsStatusResultInfo(String smsId, SmsStatusResultInfo smsStatusResultInfo);

    List<Sms> getSmsList(Predicate<Sms> filterPredicate, int packageSize);
}