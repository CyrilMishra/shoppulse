package com.shoppulse.product.entity;


import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "products")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sku ;
    private String name;
    private String description;
    private double price;
    private String category;
    private int quantity;
    private Long createdAt;

    @OneToMany(mappedBy = "productEntity", cascade = CascadeType.ALL, fetch=FetchType.LAZY)
    private List<Review> reviewList ;
}