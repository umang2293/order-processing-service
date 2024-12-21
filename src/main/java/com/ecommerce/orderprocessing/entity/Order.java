package com.ecommerce.orderprocessing.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@RequiredArgsConstructor
@Table(name = "tbl_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "productId", nullable = false)
    private Integer productId;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "productInfo", length = 250)
    private String productInfo;

}