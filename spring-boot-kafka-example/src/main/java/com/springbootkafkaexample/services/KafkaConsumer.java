package com.springbootkafkaexample.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class KafkaConsumer {

    //    @KafkaListener(topics = "#{'${kafkaTopic}'.split(',')}", groupId = "group_id")
    @KafkaListener(topics = "${kafkaTopic}")
//    @KafkaListener(topics = "#{'${KafkaTopic}'.split(',')}")
    public void consumer(@Payload List<String> messages, Acknowledgment ack) {
        log.info("Message Received of size ----> " + messages.size());
        for (String msg : messages) {
            log.info("-----> " + msg);
        }
        log.info("------------------------------------");
        ack.acknowledge();
    }

}
