package com.example.demo.entity;


import com.example.demo.enums.UserRole;
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