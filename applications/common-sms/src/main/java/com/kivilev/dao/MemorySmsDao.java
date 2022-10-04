package com.kivilev.dao;

import com.kivilev.model.Sms;
import com.kivilev.model.SmsStatusInfo;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
@Primary
public class MemorySmsDao implements SmsDao {

    ConcurrentMap<String, Sms> smsMap = new ConcurrentHashMap<>();

    @Override
    public boolean isSmsExists(String source, String sourceIdempotencyKey) {
        return smsMap.get(getUniqueKey(source, sourceIdempotencyKey)) != null;
    }

    @Override
    public List<Sms> getSmsList(Predicate<Sms> filterPredicate, int packageSize) {
        return smsMap.values().stream()
                .filter(filterPredicate)
                .limit(packageSize)
                .collect(Collectors.toList());
    }

    @Override
    public void saveSms(Sms sms) {
        smsMap.put(getUniqueKey(sms.getSourceId(), sms.getSourceIdempotencyKey()), sms);
    }


    @Override
    public void setSmsStatus(String smsId, SmsStatusInfo smsStatusInfo) {
        var smsOptional = smsMap.values().stream().filter(sms -> sms.getSmsId().equals(smsId)).findFirst();
        smsOptional.ifPresent(sms -> sms.setSmsStatusInfo(smsStatusInfo));
    }

    private String getUniqueKey(String sourceId, String sourceIdempotencyKey) {
        return String.format("%s-%s", sourceId, sourceIdempotencyKey);
    }
}
