package com.example.ecomm_orderservice.services.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {

    private static final String TOPIC = "order-notifications"; // Kafka topic

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    // Method to send messages to Kafka
    public void sendOrderNotification(String orderJson) {
        kafkaTemplate.send(TOPIC, orderJson);  // Send JSON to Kafka topic
    }
}