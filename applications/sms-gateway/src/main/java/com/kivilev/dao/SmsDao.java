package com.kivilev.dao;

import com.kivilev.service.model.Sms;
import com.kivilev.service.model.SmsResult;
import com.kivilev.service.model.SmsState;
import com.kivilev.service.model.SmsStatusResultInfo;

import java.util.List;

public interface SmsDao {

    void saveSms(Sms sms);

    void saveSmsMessages(List<Sms> smsList);

    void saveSmsStatusResultInfo(String smsId, SmsStatusResultInfo smsStatusResultInfo);

    List<Sms> getSmsMessages(SmsState smsState, SmsResult smsResult, int packageSize);

    List<Sms> findBySmsIds(List<String> smsIds);

    List<Sms> getSmsMessagesReadyForSendingQueue(int packageSize);
}