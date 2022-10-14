package com.kivilev.service.queue.model;

public record SmsSendMessageDto(
        String smsId,
        String sourceId, // TODO: убрать
        String smsText,
        String receiverPhoneNumber) {
}