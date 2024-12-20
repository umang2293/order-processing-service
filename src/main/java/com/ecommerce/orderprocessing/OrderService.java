package com.ecommerce.orderprocessing;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class OrderService {

    private final WebClient webClient;
    private final RestTemplate restTemplate;
    private final OrderRepository orderRepository;

    public OrderService(WebClient.Builder webClientBuilder, RestTemplate restTemplate, OrderRepository orderRepository  ) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8081").build();
        this.restTemplate = restTemplate;
        this.orderRepository = orderRepository;
    }

    // Synchronous call using RestTemplate
    public Order createOrder(OrderDto orderDto) {
        ProductResponse productResponse = restTemplate.getForObject("http://localhost:8081/api/v1/products/" + orderDto.getProductId(), ProductResponse.class);
        if(productResponse != null) {
            Order order = new Order();
            order.setProductId(orderDto.getProductId());
            order.setQuantity(orderDto.getQuantity());
            order.setProductInfo(productResponse.getName());
            order.setDescription(productResponse.getDescription());
            orderRepository.save(order);
            return order;
        } else {
            return null;
        }
    }

    // Asynchronous call using WebClient
    public Mono<String> getProductInfoAsync(Long productId) {
        return webClient.get()
                .uri("/api/v1/products/" + productId)
                .retrieve()
                .bodyToMono(String.class);
    }
}
