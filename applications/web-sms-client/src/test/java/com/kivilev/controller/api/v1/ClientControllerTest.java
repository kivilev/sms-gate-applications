package com.kivilev.controller.api.v1;

import com.kivilev.WebSmsClientApplication;
import com.kivilev.exception.ClientNotFoundException;
import com.kivilev.service.model.Sms;
import com.kivilev.service.model.SmsStatus;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(
        classes = WebSmsClientApplication.class,
        properties = "logging.level.com.kivilev=DEBUG",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
class ClientControllerTest extends ControllerTestBase {

    private static final String GET_SMS_MESSAGES_PATH = "/api/v1/client/1/sms?limit=10";
    private static final String EXPECTED_RESPONSE_FOR_GET_SMS_MESSAGES = """
            [{"smsId":2,"smsStatus":"SENT","createDateTime":"2000-01-01T01:01:01Z","sendDateTime":"2020-01-01T01:01:01Z"}]""";
    private static final int LIMIT = 10;
    private static final Clock CLOCK = Clock.systemUTC();
    private static final Long CLIENT_ID = 1L;
    private static final String SMS_TEXT = "sms-text";
    private static final String RECEIVER_PHONE_NUMBER = "0000000";
    private static final String IDEMPOTENCY_KEY = "key";
    private static final Long SMS_ID = 2L;
    private static final String SOURCE_ID = "sourceId";
    private static final ZonedDateTime CREATE_DATE_TIME = ZonedDateTime.of(2000, 01, 01, 01, 01, 01, 00, CLOCK.getZone());
    private static final ZonedDateTime UPDATE_DATE_TIME = ZonedDateTime.of(2020, 01, 01, 01, 01, 01, 00, CLOCK.getZone());

    @Test
    void gettingSmsMessagesShouldShouldBeSuccesfull() {
        Sms responseSms = new Sms(SMS_ID, CLIENT_ID, SOURCE_ID, IDEMPOTENCY_KEY, SMS_TEXT, RECEIVER_PHONE_NUMBER, SmsStatus.SENT, CREATE_DATE_TIME, UPDATE_DATE_TIME);
        when(smsService.getSmsMessages(CLIENT_ID, LIMIT)).thenReturn(List.of(responseSms));

        var response = ControllerUtils.executeGetRequest(mockMvc, GET_SMS_MESSAGES_PATH, MockMvcResultMatchers.status().is2xxSuccessful());

        assertEquals(EXPECTED_RESPONSE_FOR_GET_SMS_MESSAGES, response);
    }

    @Test
    void gettingSmsMessagesForNonExistClientShouldGet404Response() {
        when(smsService.getSmsMessages(CLIENT_ID, LIMIT)).thenThrow(new ClientNotFoundException());

        var response = ControllerUtils.executeGetRequest(mockMvc, GET_SMS_MESSAGES_PATH, MockMvcResultMatchers.status().is4xxClientError());

        assertEquals("Client not found", response);
    }
}