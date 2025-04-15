package com.example.ecomm_orderservice.dto;


import lombok.Data;

import java.util.List;

@Data
public class OrderedProductsResponseDto {

    private List<ProductDto> productDtoList;

    private Long orderAmount;
}