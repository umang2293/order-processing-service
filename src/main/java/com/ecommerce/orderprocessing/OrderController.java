package com.ecommerce.orderprocessing;

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
}
