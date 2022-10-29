package com.kivilev.controller.api.v1.model;

import java.time.ZonedDateTime;

public record SendNewSmsResponseDto(
        Long smsId,
        SmsStatusDto smsStatus,
        ZonedDateTime createDateTime,
        ZonedDateTime sendDateTime
) {
}
