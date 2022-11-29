package com.springbootkafkaexample;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author ravikant_kondepati
 */
public class KafkaExample {
    public static void main(String[] args) throws JsonProcessingException {
        test3();
    }

    public static void test3() throws JsonProcessingException {
        String keyStoreLocation = "msk.jks";
        String keyStorePassword = "Eusv37yqsXDoARrLMpfckuK0L";
        String bootstrapServers = "localhost:9090";
        String topic = "testing_topic";
        Properties properties = new Properties();
        properties.put("bootstrap.servers", bootstrapServers);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        properties.put("security.protocol", "SSL");
        properties.put("ssl.keystore.location", keyStoreLocation);
        properties.put("ssl.keystore.password", keyStorePassword);
        Map<String, String> json = new HashMap<>();
        String docId = "VDCB81)VeC";
        json.put("country", "US");
        json.put("_id", docId);
        json.put("id", docId);
        ObjectMapper objectMapper = new ObjectMapper();
        String msg = objectMapper.writeValueAsString(json);
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, msg);
        producerRecord.headers().add("ApiKey", "org".getBytes());
        producerRecord.headers().add("DocId", docId.getBytes());
        producerRecord.headers().add("UpdateType", "1".getBytes());
        KafkaProducer<String, String> myProducer = new KafkaProducer<>(properties);
        try {
            myProducer.send(producerRecord, (recordMetadata, e) -> {
                if (e == null) {
                    System.out.println("Successfully received the details as: \n" +
                            "Topic:" + recordMetadata.topic() + "\n" +
                            "Partition:" + recordMetadata.partition() + "\n" +
                            "Offset" + recordMetadata.offset() + "\n" +
                            "Timestamp" + recordMetadata.timestamp());
                } else {
                    System.out.println("Can't produce,getting error " + e);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            myProducer.close();
        }
    }


}
