package com.kivilev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication

public class SmsGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmsGatewayApplication.class, args);
    }
}
