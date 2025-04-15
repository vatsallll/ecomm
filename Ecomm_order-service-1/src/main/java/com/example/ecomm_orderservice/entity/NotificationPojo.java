package com.example.ecomm_orderservice.entity;

import lombok.Data;

import java.util.UUID;

@Data
public class NotificationPojo {
    private String email;

    private String name;

    private String status;

    private UUID trackingId;
}
