package com.kivilev.controller.api.v1;

import com.kivilev.controller.api.v1.mapper.SmsRequestResponseMapper;
import com.kivilev.controller.api.v1.model.SendNewSmsRequestDto;
import com.kivilev.controller.api.v1.model.SendNewSmsResponseDto;
import com.kivilev.exception.ClientNotFoundException;
import com.kivilev.service.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import static com.kivilev.controller.api.v1.RestError.ERROR_MESSAGE_CLIENT_NOT_FOUND;

@RestController
@RequestMapping("/api/v1/sms")
public class SmsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SmsController.class);
    private final SmsService smsService;
    private final SmsRequestResponseMapper smsRequestResponseMapper;

    public SmsController(SmsService smsService,
                         SmsRequestResponseMapper smsRequestResponseMapper) {
        this.smsService = smsService;
        this.smsRequestResponseMapper = smsRequestResponseMapper;
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.OK)
    public SendNewSmsResponseDto sendNewSms(@RequestBody SendNewSmsRequestDto sendNewSmsRequestDto) {
        LOGGER.debug("got request. {}", sendNewSmsRequestDto.toString());

        try {
            var newSms = smsService.sendNewSms(smsRequestResponseMapper.toSms(sendNewSmsRequestDto));
            return smsRequestResponseMapper.toSendNewSmsResponseDto(newSms);
        } catch (ClientNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ERROR_MESSAGE_CLIENT_NOT_FOUND);
        }
    }
}
