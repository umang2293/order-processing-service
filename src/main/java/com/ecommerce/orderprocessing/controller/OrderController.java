package com.ecommerce.orderprocessing.controller;

import com.ecommerce.orderprocessing.dto.OrderDto;
import com.ecommerce.orderprocessing.service.OrderService;
import com.ecommerce.orderprocessing.entity.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/addOrder")
    public Order createOrder(@RequestBody OrderDto orderDto) {
        return orderService.createOrder(orderDto);
    }

    @GetMapping("/async/{productId}")
    public Mono<String> getProductInfoAsync(@PathVariable Long productId) {
        return orderService.getProductInfoAsync(productId);
    }

    @GetMapping("/getOrder/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }
}
