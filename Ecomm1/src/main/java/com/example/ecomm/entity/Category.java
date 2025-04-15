package com.example.ecomm.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "category")
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Lob
    private String description;

//    @OneToMany(fetch = FetchType.LAZY)
//    @JoinColumn(name = "category_id", nullable = false)
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    private List<Order> orders;

}
