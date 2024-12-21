package com.ecommerce.orderprocessing.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class OrderDto {

    private Integer id;
    private Integer productId;
    private String description;
    private Integer quantity;
    private String productInfo;
}
