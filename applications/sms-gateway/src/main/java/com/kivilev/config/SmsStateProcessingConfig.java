package com.kivilev.config;

import com.kivilev.service.model.SmsState;
import com.kivilev.service.processor.SmsStateProcessor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@ConfigurationProperties(prefix = "state-processor")
@Configuration
public class SmsStateProcessingConfig {
    private Integer packageSize = 15;

    /*public SmsStateProcessorConfig(Integer packageSize) {
        this.packageSize = packageSize != null ? packageSize : 10;
    }*/

    @Bean
    Map<SmsState, SmsStateProcessor> getSmsStateProcessorMap(List<SmsStateProcessor> smsStateProcessors) {
        return smsStateProcessors.stream().collect(Collectors.toUnmodifiableMap(SmsStateProcessor::getSmsStatus, Function.identity()));
    }

    public Integer getPackageSize() {
        return packageSize;
    }

    public void setPackageSize(Integer packageSize) {
        this.packageSize = packageSize;
    }
}
