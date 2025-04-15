package com.example.ecomm_productservice.dto;

import lombok.Data;

@Data
public class FAQDto {

    private long id;

    private String question;

    private String answer;

    private Long productId;
}