package com.kivilev.controller.api.v1.model;

public record SendNewSmsRequestDto(Long clientId,
                                   String smsText,
                                   String receiverPhoneNumber,
                                   String idempotencyKey) {
}
