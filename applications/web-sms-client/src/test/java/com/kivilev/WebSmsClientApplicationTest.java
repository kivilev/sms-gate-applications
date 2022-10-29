package com.kivilev;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
        classes = WebSmsClientApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class WebSmsClientApplicationTest {
    @Test
    public void runApplication() {
    }
}