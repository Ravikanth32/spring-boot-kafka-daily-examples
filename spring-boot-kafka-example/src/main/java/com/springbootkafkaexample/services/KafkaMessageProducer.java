package com.springbootkafkaexample.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
@Slf4j
public class KafkaMessageProducer {
    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Value("${KafkaIndexPipelineAPIKey}")
    String apiKey;

    @Value("${kafkaTopic}")
    String topic;

    public void publishMessage(String message, String docId, String updateType) {
        log.info("Publishing message");
        sendMessage1(topic, message, docId, updateType);
        log.info("Message published");
    }

    private void sendMessage1(String topic, String message, String docId, String updateType) {
        ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, message);
        record.headers().add("ApiKey", apiKey.getBytes());
        record.headers().add("UpdateType", updateType.getBytes());
        record.headers().add("DocId", docId.getBytes());
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(record);
        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("Sent message=[" + message + "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }

            @Override
            public void onFailure(Throwable ex) {
                log.info("Unable to send message=[" + message + "] due to : " + ex.getMessage());
            }
        });
    }

    private void sendMessage(String topic, String id, String message) {
        ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, message);
        record.headers().add("ApiKey", apiKey.getBytes());
        record.headers().add("UpdateType", "0".getBytes());
        record.headers().add("DocId", id.getBytes());
        // example for file lookup: record.headers().add("FilePrimaryKey", id.getBytes());

        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(record);
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("Sent message=[" + message + "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }

            @Override
            public void onFailure(Throwable ex) {
                log.info("Unable to send message=[" + message + "] due to : " + ex.getMessage());
            }
        });
    }
}
