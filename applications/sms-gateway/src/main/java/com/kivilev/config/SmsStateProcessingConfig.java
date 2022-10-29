package com.kivilev.config;

import com.kivilev.service.model.SmsState;
import com.kivilev.service.processor.SmsStateProcessor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@ConfigurationProperties(prefix = "state-processor")
@ConstructorBinding
public class SmsStateProcessingConfig {
    private final int packageSize;

    public SmsStateProcessingConfig(int packageSize) {
        this.packageSize = packageSize;
    }

    @Bean
    Map<SmsState, SmsStateProcessor> getSmsStateProcessorMap(List<SmsStateProcessor> smsStateProcessors) {
        return smsStateProcessors.stream().collect(Collectors.toUnmodifiableMap(SmsStateProcessor::getSmsState, Function.identity()));
    }

    public Integer getPackageSize() {
        return packageSize;
    }
}