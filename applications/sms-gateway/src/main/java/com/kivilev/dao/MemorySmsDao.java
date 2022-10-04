package com.kivilev.dao;

import com.kivilev.service.model.Sms;
import com.kivilev.service.model.SmsStatusResultInfo;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class MemorySmsDao {//implements SmsDao {
/*    ConcurrentMap<String, Sms> smsMap = new ConcurrentHashMap<>();

    @Override
    public boolean isExists(String smsId) {
        return smsMap.get(smsId) != null;
    }

    @Override
    public void saveSms(Sms sms) {
        smsMap.put(sms.getSmsId(), sms);
    }

    @Override
    public void saveSmsStatusResultInfo(String smsId, SmsStatusResultInfo smsStatusResultInfo) {
        var sms = smsMap.get(smsId);
        if (sms != null) {
            sms.setSmsStatusInfo(smsStatusResultInfo);
        }
    }

    @Override
    public List<Sms> getSmsList(Predicate<Sms> filterPredicate, int packageSize) {
        return smsMap.values().stream()
                .filter(filterPredicate)
                .limit(packageSize)
                .collect(Collectors.toList());
    }*/
}
