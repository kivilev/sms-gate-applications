package com.kivilev.controller.api.v1;

import com.kivilev.WebSmsClientApplication;
import com.kivilev.exception.ClientNotFoundException;
import com.kivilev.service.model.Sms;
import com.kivilev.service.model.SmsStatus;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(classes = WebSmsClientApplication.class, properties = "logging.level.com.kivilev=DEBUG")
@AutoConfigureMockMvc
class ClientControllerTest extends ControllerTestBase {

    private final static String GET_SMS_MESSAGES_PATH = "/api/v1/client/1/sms?limit=10";
    protected final static String EXPECTED_RESPONSE_FOR_GET_SMS_MESSAGES = """
            [{"smsId":2,"smsStatus":"SENT","createDateTime":"2000-01-01T01:01:01Z","sendDateTime":"2020-01-01T01:01:01Z"}]""";
    private final static int LIMIT = 10;
    private final static Clock clock = Clock.systemUTC();
    private final static Long CLIENT_ID = 1L;
    private final static String SMS_TEXT = "sms-text";
    private final static String RECEIVER_PHONE_NUMBER = "0000000";
    private final static String IDEMPOTENCY_KEY = "key";
    private static final Long SMS_ID = 2L;
    private static final String SOURCE_ID = "sourceId";
    private static final ZonedDateTime CREATE_DATE_TIME = ZonedDateTime.of(2000, 01, 01, 01, 01, 01, 00, clock.getZone());
    private static final ZonedDateTime UPDATE_DATE_TIME = ZonedDateTime.of(2020, 01, 01, 01, 01, 01, 00, clock.getZone());

    @Test
    void GettingSmsMessagesShouldShouldBeSuccesfull() {
        Sms responseSms = new Sms(SMS_ID, CLIENT_ID, SOURCE_ID, IDEMPOTENCY_KEY, SMS_TEXT, RECEIVER_PHONE_NUMBER, SmsStatus.SENT, CREATE_DATE_TIME, UPDATE_DATE_TIME);
        Mockito.when(smsService.getSmsMessages(CLIENT_ID, LIMIT)).thenReturn(List.of((responseSms)));

        var response = ControllerUtils.executeGetRequest(mockMvc, GET_SMS_MESSAGES_PATH, MockMvcResultMatchers.status().is2xxSuccessful());

        assertEquals(EXPECTED_RESPONSE_FOR_GET_SMS_MESSAGES, response);
    }

    @Test
    void GettingSmsMessagesForNonExistClientShouldGet404Response() {
        Mockito.when(smsService.getSmsMessages(CLIENT_ID, LIMIT)).thenThrow(new ClientNotFoundException());

        var response = ControllerUtils.executeGetRequest(mockMvc, GET_SMS_MESSAGES_PATH, MockMvcResultMatchers.status().is4xxClientError());

        assertEquals("Client not found", response);
    }
}