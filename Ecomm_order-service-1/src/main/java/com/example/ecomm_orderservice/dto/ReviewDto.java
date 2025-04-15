package com.example.ecomm_orderservice.dto;


import com.example.ecomm_orderservice.entity.Product;
import com.example.ecomm_orderservice.entity.User;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ReviewDto {

    private Long id;

    private Long rating;

    private String description;

    private MultipartFile img;

    private byte[] returnedImg;

    private Long userId;


    private Long productId;

    private String username;

}
