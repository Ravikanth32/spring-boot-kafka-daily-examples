package com.springbootkafkaexample.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class KafkaConsumer {

    @Value("${kafkaTopic}")
    String topic;

    //    @KafkaListener(topics = "#{'${kafkaTopic}'.split(',')}", groupId = "group_id")
    @KafkaListener(topics = "${kafkaTopic}")
    public void receiveMessage(@Payload List<String> messages, Acknowledgment ack) {
        System.out.println("---->" + messages);
    }

}
