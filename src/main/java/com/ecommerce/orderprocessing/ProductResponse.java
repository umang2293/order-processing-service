package com.ecommerce.orderprocessing;

import lombok.Data;

@Data
public class ProductResponse {

    private Integer id;
    private String name;
    private String description;
    private Double price;
    private Integer quantity;
}
