package com.kivilev.service.queue.model;

public record SmsStatusChangeMessageDto(String smsId, String sourceId,
                                        SmsStatusDto status, String errorCode,
                                        String errorMessage) {
}