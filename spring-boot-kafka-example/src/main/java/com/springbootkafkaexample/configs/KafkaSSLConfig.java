package com.springbootkafkaexample.configs;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.kafka.ssl")
public class KafkaSSLConfig {
    public static String SECURITY_PROTOCOL_SSL = "SSL";
    /**
     * Location of the key store file.
     */
    @Value("${KafkaSSLEnabled:#{false}}")
    private boolean sslEnabled;

    /**
     * Location of the key store file.
     */
    @Value("${key-store-location:#{null}}")
    private String keyStoreLocation;

    /**
     * Store password for the key store file.
     */
    @Value("${key-store-password:#{null}}")
    private String keyStorePassword;
}
