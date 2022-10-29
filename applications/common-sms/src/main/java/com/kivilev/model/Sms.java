package com.kivilev.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import javax.annotation.Nonnull;
import java.util.Objects;

@Table("sms")
public class Sms implements Persistable<Long> {
    @Id
    private Long smsId;
    private final Long clientId;
    private final String sourceId;
    private final String sourceIdempotencyKey;
    private final String smsText;
    private final String receiverPhoneNumber;
    private final String createDateTime;
    private final String changeStatusDateTime;

    /*
    TODO: переписать на нормальное использование дат
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private final String createDateTime;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private final String changeStatusDateTime;*/

    private SmsStateDetail smsStateDetail;

    @Transient
    private boolean isNew = true;
    // TODO: скорее всего не нужно

    public Sms(Long smsId, String sourceId, Long clientId, String sourceIdempotencyKey, String smsText,
               String receiverPhoneNumber, SmsStateDetail smsStateDetail, String createDateTime,
               String changeStatusDateTime, boolean isNew) {
        this.smsId = smsId;
        this.clientId = clientId;
        this.sourceId = sourceId;
        this.sourceIdempotencyKey = sourceIdempotencyKey;
        this.smsText = smsText;
        this.receiverPhoneNumber = receiverPhoneNumber;
        this.smsStateDetail = smsStateDetail;
        this.isNew = isNew;
        this.createDateTime = createDateTime;
        this.changeStatusDateTime = changeStatusDateTime;
    }

    @PersistenceCreator
    public Sms(Long smsId, String sourceId, Long clientId, String sourceIdempotencyKey, String smsText,
               String receiverPhoneNumber, SmsStateDetail smsStateDetail, String createDateTime, String changeStatusDateTime) {
        this(smsId, sourceId, clientId, sourceIdempotencyKey, smsText, receiverPhoneNumber, smsStateDetail, createDateTime, changeStatusDateTime, false);
    }

    public Long getSmsId() {
        return smsId;
    }

    public void setSmsId(Long smsId) {
        this.smsId = smsId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public String getSmsText() {
        return smsText;
    }

    public String getReceiverPhoneNumber() {
        return receiverPhoneNumber;
    }

    public SmsStateDetail getSmsStatusInfo() {
        return smsStateDetail;
    }

    public void setSmsStatusInfo(SmsStateDetail smsStateDetail) {
        this.smsStateDetail = smsStateDetail;
    }

    public String getSourceIdempotencyKey() {
        return sourceIdempotencyKey;
    }

    @Nonnull
    public Long getClientId() {
        return clientId;
    }

    @Override
    public Long getId() {
        return smsId;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sms sms = (Sms) o;
        return smsId.equals(sms.smsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(smsId);
    }

    public String getCreateDateTime() {
        return createDateTime;
    }

    public String getChangeStatusDateTime() {
        return changeStatusDateTime;
    }
}
