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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;


@SpringBootTest(
        classes = WebSmsClientApplication.class,
        properties = "logging.level.com.kivilev=DEBUG",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
class SmsControllerTest extends ControllerTestBase {

    private static final String SEND_NEW_SMS_PATH = "/api/v1/sms";
    private static final String SEND_NEW_SMS_JSON = """
            {
              "clientId": 1,
              "smsText": "Привет! это тестовая SMS",
              "receiverPhoneNumber": "+7-999-999-00-00",
              "idempotencyKey": "key-433"
            }
            """;
    private static final String EXPECTED_RESPONSE_FOR_SEND_NEW_SMS = """
            {"smsId":2,"smsStatus":"SENT","createDateTime":"2000-01-01T01:01:01Z","sendDateTime":"2020-01-01T01:01:01Z"}""";
    private static final Clock clock = Clock.systemUTC();
    private static final Long CLIENT_ID = 1L;
    private static final String SMS_TEXT = "sms-text";
    private static final String RECEIVER_PHONE_NUMBER = "0000000";
    private static final String IDEMPOTENCY_KEY = "key";
    private static final Long SMS_ID = 2L;
    private static final String SOURCE_ID = "sourceId";
    private static final ZonedDateTime CREATE_DATE_TIME = ZonedDateTime.of(2000, 01, 01, 01, 01, 01, 00, clock.getZone());
    private static final ZonedDateTime UPDATE_DATE_TIME = ZonedDateTime.of(2020, 01, 01, 01, 01, 01, 00, clock.getZone());

    @Test
    void sendingNewCorrectSmsShouldShouldBeSuccesfull() {
        Sms responseSms = new Sms(SMS_ID, CLIENT_ID, SOURCE_ID, IDEMPOTENCY_KEY, SMS_TEXT, RECEIVER_PHONE_NUMBER, SmsStatus.SENT, CREATE_DATE_TIME, UPDATE_DATE_TIME);
        Mockito.when(smsService.sendNewSms(any())).thenReturn(responseSms);

        var response = ControllerUtils.executePostRequest(mockMvc, SEND_NEW_SMS_PATH, SEND_NEW_SMS_JSON, MockMvcResultMatchers.status().is2xxSuccessful());

        assertEquals(EXPECTED_RESPONSE_FOR_SEND_NEW_SMS, response);
    }

    @Test
    void sendingNewCorrectSmsWithNonExistClientShouldGet404Response() {
        Mockito.when(smsService.sendNewSms(any())).thenThrow(new ClientNotFoundException());

        var response = ControllerUtils.executePostRequest(mockMvc, SEND_NEW_SMS_PATH, SEND_NEW_SMS_JSON, MockMvcResultMatchers.status().is4xxClientError());

        assertEquals("Client not found", response);
    }
}