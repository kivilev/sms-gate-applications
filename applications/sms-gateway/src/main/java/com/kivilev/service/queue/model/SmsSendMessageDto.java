package com.kivilev.service.queue.model;

public record SmsSendMessageDto(
        String smsId,
        String smsText,
        String receiverPhoneNumber) {
}