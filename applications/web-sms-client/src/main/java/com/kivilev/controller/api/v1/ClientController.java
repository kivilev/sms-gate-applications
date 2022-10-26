package com.kivilev.controller.api.v1;

import com.kivilev.controller.api.v1.mapper.SmsRequestResponseMapper;
import com.kivilev.controller.api.v1.model.SendNewSmsResponseDto;
import com.kivilev.exception.ClientNotFoundException;
import com.kivilev.service.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static com.kivilev.controller.api.v1.RestError.ERROR_MESSAGE_CLIENT_NOT_FOUND;

@RestController
@RequestMapping("/api/v1/client")
public class ClientController {

    private final SmsService smsService;
    private final SmsRequestResponseMapper smsRequestResponseMapper;
    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

    public ClientController(SmsService smsService,
                            SmsRequestResponseMapper smsRequestResponseMapper) {
        this.smsService = smsService;
        this.smsRequestResponseMapper = smsRequestResponseMapper;
    }

    @GetMapping("/{clientId}/sms")
    @ResponseStatus(HttpStatus.OK)
    public List<SendNewSmsResponseDto> getSmsMessages(@PathVariable("clientId") Long clientId, Integer limit) {
        logger.debug(String.format("got request. clientId: %s, limit: %s", clientId, limit));

        try {
            return smsService.getSmsMessages(clientId, limit)
                    .stream()
                    .map(smsRequestResponseMapper::toSendNewSmsResponseDto)
                    .toList();
        } catch (ClientNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ERROR_MESSAGE_CLIENT_NOT_FOUND);
        }
    }
}
