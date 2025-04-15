package com.example.ecomm_orderservice.dto;

import com.example.ecomm_orderservice.entity.CartItems;
import com.example.ecomm_orderservice.entity.User;
import com.example.ecomm_orderservice.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class OrderDto {

    private long id;

    private String orderDescription;

    private Date date;

    private Long amount;

    private String address;

    private String payment;

    private OrderStatus orderStatus;

    private Long totalAmount;

//    private Long discount;

    private UUID trackingId;

    private String userName;

    private List<CartItemsDto> cartItems;
}
