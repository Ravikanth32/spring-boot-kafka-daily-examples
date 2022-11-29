package com.springbootkafkaexample.configs;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.CommonClientConfigs.SECURITY_PROTOCOL_CONFIG;
import static org.apache.kafka.common.config.SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG;
import static org.apache.kafka.common.config.SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG;

@Configuration
@Slf4j
public class KafkaProducerConfig {

    @Value("${KafkaBroker}")
    private String bootstrapServers;
    @Autowired
    private KafkaSSLConfig sslConfig;

    @Bean
    public KafkaTemplate getKafkaTemplate() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServers);
        configProps.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        configProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        if (sslConfig.isSslEnabled()) {
            configProps.put(SECURITY_PROTOCOL_CONFIG, KafkaSSLConfig.SECURITY_PROTOCOL_SSL);
            configProps.put(SSL_KEYSTORE_PASSWORD_CONFIG, sslConfig.getKeyStorePassword());
            configProps.put(SSL_KEYSTORE_LOCATION_CONFIG, sslConfig.getKeyStoreLocation());
        }
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(configProps));
    }
}
