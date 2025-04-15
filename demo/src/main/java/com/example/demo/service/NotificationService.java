package com.example.demo.service;

import com.example.demo.entity.NotificationPojo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private JavaMailSender mailSender;

    @KafkaListener(topics = "order-notifications", groupId = "notification-group")
    public void consumeOrderNotification(String orderJson) {
        System.out.println("Received Order Notification: " + orderJson);

        try {
            // Parse the JSON into a NotificationPojo object
            ObjectMapper objectMapper = new ObjectMapper();
            NotificationPojo notification = objectMapper.readValue(orderJson, NotificationPojo.class);

            // Send email notification
            sendEmail(notification.getEmail(), "Order Status Update",
                    "Hello " + notification.getName() + ",\n\n" +
                            "Your order status with trackingId " + notification.getTrackingId() + " has been updated to: " + notification.getStatus() + ".\n\n" +
                            "Thank you for shopping with us!");

            System.out.println("Email sent to: " + notification.getEmail());

        } catch (Exception e) {
            System.err.println("Error processing notification: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
            System.out.println("Email successfully sent to " + to);
        } catch (Exception e) {
            System.err.println("Failed to send email to " + to + ": " + e.getMessage());
        }
    }
}
