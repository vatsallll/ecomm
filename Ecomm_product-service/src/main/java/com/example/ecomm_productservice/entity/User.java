package com.example.ecomm_productservice.entity;

//import com.example.ecomm.enums.UserRole;
import com.example.ecomm_productservice.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;

    private String name;
    private UserRole role;

    @Lob
    @Column
    private byte[] img;

    // Getters and Setters
}