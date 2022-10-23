package com.kivilev.controller.api.v1;

import com.kivilev.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

abstract class ControllerTestBase {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    SmsService smsService;
}
