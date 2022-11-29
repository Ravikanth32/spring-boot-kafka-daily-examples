package com.springbootkafkaexample.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springbootkafkaexample.services.KafkaMessageProducer;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {

    @Autowired
    KafkaMessageProducer kafkaMessageProducer;
    private ObjectMapper objectMapper = new ObjectMapper();
    @GetMapping("/send/{docId}")
    public String sendMessage(@PathVariable("docId") String docId) throws JsonProcessingException {
        Map<String, String> json = new HashMap<>();
        json.put("country", "US");
        json.put("_id", docId);
        json.put("id", docId);
        String msg = objectMapper.writeValueAsString(json);
        kafkaMessageProducer.publishMessage(msg, docId, "0");
        return "Sent Successfully";
    }

}
