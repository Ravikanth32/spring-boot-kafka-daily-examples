package com.springbootkafkaexample.configs;

import com.springbootkafkaexample.filter.KafkaMessageFilter;
import com.springbootkafkaexample.services.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.BatchLoggingErrorHandler;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.CommonClientConfigs.SECURITY_PROTOCOL_CONFIG;
import static org.apache.kafka.common.config.SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG;
import static org.apache.kafka.common.config.SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG;

@Configuration
@EnableKafka
public class KafkaReceiverConfig {

    @Value("${GROUP_ID:app-service}")
    protected String groupId;
    @Autowired
    private KafkaSSLConfig sslConfig;
    @Value("${KafkaBroker}")
    private String bootstrapServers;
    @Value("${kafka.offset}")
    private String kafkaOffset;
    @Value("${MaxPollSizeKafka:5}")
    private int maxPollRecordsSize;
    @Value("${kafka.max.poll.interval.ms}")
    private int maxPollInterval;
    @Value("${kafka.fetch.max.wait.ms}")
    private int maxFetchWaitingTime;

    @Autowired
    private KafkaMessageFilter kafkaMessageFilter;

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        // allows a pool of processes to divide the work of consuming and
        // processing records
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        // automatically reset the offset to the earliest offset
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaOffset);
        props.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, maxFetchWaitingTime);
        //props.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, minFetchBytes);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecordsSize);
        //props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeOut);
        //props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, heartbeatInterval);
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, maxPollInterval);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        if (sslConfig.isSslEnabled()) {
            props.put(SECURITY_PROTOCOL_CONFIG, KafkaSSLConfig.SECURITY_PROTOCOL_SSL);
            props.put(SSL_KEYSTORE_LOCATION_CONFIG, sslConfig.getKeyStoreLocation());
            props.put(SSL_KEYSTORE_PASSWORD_CONFIG, sslConfig.getKeyStorePassword());
        }
        return props;
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setBatchListener(true);
        factory.setBatchErrorHandler(new BatchLoggingErrorHandler());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.setRecordFilterStrategy(kafkaMessageFilter);
        factory.setAckDiscarded(true);
        return factory;
    }

    @Bean
    public KafkaConsumer receiver() {
        return new KafkaConsumer();
    }

}
