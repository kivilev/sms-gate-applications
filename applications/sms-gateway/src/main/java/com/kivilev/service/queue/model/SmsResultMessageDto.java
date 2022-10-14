package com.kivilev.service.queue.model;

public record SmsResultMessageDto(String smsId,
                                  SmsResultDto result,
                                  String errorCode,
                                  String errorMessage) {
}

