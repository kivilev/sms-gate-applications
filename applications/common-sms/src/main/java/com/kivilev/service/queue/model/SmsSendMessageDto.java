package com.kivilev.service.queue.model;

public record SmsSendMessageDto(
        String smsId,
        String sourceId,
        String smsText,
        String receiverPhoneNumber) {
}