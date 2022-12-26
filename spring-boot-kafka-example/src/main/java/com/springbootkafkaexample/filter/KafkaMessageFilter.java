package com.springbootkafkaexample.filter;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.adapter.RecordFilterStrategy;
import org.springframework.stereotype.Component;

@Component
public class KafkaMessageFilter implements RecordFilterStrategy<String, String> {
    @Override
    public boolean filter(ConsumerRecord<String, String> consumerRecord) {

        consumerRecord.headers().forEach(
                header -> {

                });
        return false;
    }
}
