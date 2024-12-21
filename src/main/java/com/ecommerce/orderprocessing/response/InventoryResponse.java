package com.ecommerce.orderprocessing.response;

import lombok.Data;

@Data
public class InventoryResponse {

    private Long id;
    private Long productid;
    private Integer quantity;
}
