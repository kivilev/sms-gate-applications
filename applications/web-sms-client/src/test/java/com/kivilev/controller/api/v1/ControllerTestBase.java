package com.kivilev.controller.api.v1;

import com.kivilev.service.ClientService;
import com.kivilev.service.CommonSmsService;
import com.kivilev.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

abstract class ControllerTestBase {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    SmsService smsService;

    @MockBean
    ClientService clientService;

    @MockBean
    CommonSmsService commonSmsService;

    // TODO: Сделать нормальное мокирование grpc - либо отключение полностью
    /*
    @GrpcClient("common-sms-service")
    @MockBean
    private SmsSendServiceGrpc.SmsSendServiceBlockingStub smsSendServiceStub;
    */
}
