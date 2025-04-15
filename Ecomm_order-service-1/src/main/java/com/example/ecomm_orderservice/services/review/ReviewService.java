package com.example.ecomm_orderservice.services.review;

import com.example.ecomm_orderservice.dto.OrderedProductsResponseDto;
import com.example.ecomm_orderservice.dto.ReviewDto;

import java.io.IOException;

public interface ReviewService {

    OrderedProductsResponseDto getOrderedProductsDetailsByOrderId(Long orderId);

    ReviewDto giveReview (ReviewDto reviewDto) throws IOException;
}