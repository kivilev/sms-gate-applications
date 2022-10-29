package com.kivilev.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "state-processor")
@ConstructorBinding
public class SmsStateProcessingConfig {
    private final int packageSize;

    public SmsStateProcessingConfig(int packageSize) {
        this.packageSize = packageSize;
    }

    public Integer getPackageSize() {
        return packageSize;
    }
}