package com.kivilev.dao;

import com.kivilev.service.model.Sms;
import com.kivilev.service.model.SmsResult;
import com.kivilev.service.model.SmsState;
import com.kivilev.service.model.SmsStateDetail;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Repository
public class InMemorySmsDao implements SmsDao {

    ConcurrentMap<String, Sms> smsMap = new ConcurrentHashMap<>();

    @Override
    public void saveSms(Sms sms) {
        smsMap.put(sms.getSmsId(), sms);
    }

    @Override
    public void saveSmsMessages(List<Sms> smsList) {
        smsList.forEach(sms -> {
            smsMap.put(sms.getSmsId(), sms);
        });
    }

    @Override
    public void saveSmsStatusResultInfo(String smsId, SmsStateDetail smsStateDetail) {
        var sms = smsMap.get(smsId);
        if (sms != null) {
            sms.setSmsStatusDetail(smsStateDetail);
        }
    }

    @Override
    public List<Sms> getSmsMessages(SmsState smsState, SmsResult smsResult, int packageSize) {
        return smsMap.values().stream()
                .filter(sms ->
                        sms.getSmsStatusDetail().getSmsStatus() == smsState &&
                                sms.getSmsStatusDetail().getSmsResult() == smsResult
                )
                .limit(packageSize)
                .collect(Collectors.toList());
    }

    @Override
    public List<Sms> findBySmsIds(List<String> smsIds) {
        return smsMap.values().stream()
                .filter(sms ->
                        smsIds.contains(sms.getSmsId())
                )
                .collect(Collectors.toList());
    }

    @Override
    public List<Sms> getSmsMessagesReadyForSendingQueue(int packageSize) {
        return smsMap.values().stream()
                .filter(this::isSmsReadyForSendingToQueue)
                .limit(packageSize)
                .collect(Collectors.toList());
    }

    private boolean isSmsReadyForSendingToQueue(Sms sms) {
        var smsResult = sms.getSmsStatusDetail().getSmsResult();

        boolean isDidNotSendToQueueBefore = !sms.isSendStatusToQueue();
        boolean isErrorProcessingResult = smsResult == SmsResult.ERROR;
        boolean isSuccessfulSendToClient = sms.getSmsStatusDetail().getSmsStatus() == SmsState.SENT_TO_CLIENT &&
                smsResult == SmsResult.SUCCESSFUL_PROCESSED;

        return isDidNotSendToQueueBefore && (isErrorProcessingResult || isSuccessfulSendToClient);
    }
}
