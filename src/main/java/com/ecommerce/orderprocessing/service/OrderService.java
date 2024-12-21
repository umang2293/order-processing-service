package com.ecommerce.orderprocessing.service;

import com.ecommerce.orderprocessing.dto.OrderDto;
import com.ecommerce.orderprocessing.repository.OrderRepository;
import com.ecommerce.orderprocessing.response.ProductResponse;
import com.ecommerce.orderprocessing.entity.Order;
import com.ecommerce.orderprocessing.exception.InsufficientInventoryException;
import com.ecommerce.orderprocessing.response.InventoryResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class OrderService {

    private final WebClient webClient;
    private final RestTemplate restTemplate;
    private final OrderRepository orderRepository;
    private final static String PRODUCT_BASE_URL = "http://localhost:8081/api/v1/products/";
    private final static String INVENTORY_BASE_URL = "http://localhost:8084/api/v1/inventory/";

    public OrderService(WebClient.Builder webClientBuilder, RestTemplate restTemplate, OrderRepository orderRepository  ) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8081").build();
        this.restTemplate = restTemplate;
        this.orderRepository = orderRepository;
    }

    public Order createOrder(OrderDto orderDto) {

        // Call Product catalog service to get product details
        String productServiceURL = PRODUCT_BASE_URL + orderDto.getProductId();
        ProductResponse productResponse = restTemplate.getForObject(productServiceURL, ProductResponse.class);

        // Call Inventory service to check availability
        String inventoryServiceURL = INVENTORY_BASE_URL + "/product/availability/" + orderDto.getProductId();
        InventoryResponse inventoryResponse = restTemplate.getForObject(inventoryServiceURL, InventoryResponse.class);

        if (inventoryResponse != null && inventoryResponse.getQuantity() < orderDto.getQuantity()) {
           throw new RuntimeException("Insufficient inventory");
        }

        // Update inventory after order placed
        try{
            restTemplate.put(INVENTORY_BASE_URL + "/updateInventory/" + orderDto.getProductId() + "/" + orderDto.getQuantity(), null);
            if(productResponse != null && inventoryResponse != null) {
                Order order = new Order();
                order.setProductId(orderDto.getProductId());
                order.setQuantity(orderDto.getQuantity());
                order.setProductInfo(productResponse.getName());
                order.setDescription(productResponse.getDescription());
                return orderRepository.save(order);
            } else {
                return null;
            }
        } catch(HttpClientErrorException.BadRequest e) {
            String errorMessage = e.getResponseBodyAsString();
            throw new InsufficientInventoryException("Inventory update failed: " + errorMessage);
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred: " + e.getMessage());
        }
    }

    // Asynchronous call using WebClient
    public Mono<String> getProductInfoAsync(Long productId) {
        return webClient.get()
                .uri("/api/v1/products/" + productId)
                .retrieve()
                .bodyToMono(String.class);
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }
}
