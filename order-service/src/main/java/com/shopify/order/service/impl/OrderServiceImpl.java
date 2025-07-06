package com.shopify.order.service.impl;

import com.shopify.order.commons.dto.InventoryResponseDto;
import com.shopify.order.dto.OrderLineItemsDto;
import com.shopify.order.dto.OrderRequest;
import com.shopify.order.dto.OrderResponse;
import com.shopify.order.model.Order;
import com.shopify.order.model.OrderLineItems;
import com.shopify.order.repository.OrderRepository;
import com.shopify.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private  final OrderRepository orderRepository;
    private final WebClient webClient;


    @Override
    public OrderResponse createOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        // Create and attach order line items
        List<OrderLineItems> orderLineItemsList = orderRequest.orderLineItemsDtoList().stream()
                .map(dto -> {
                    OrderLineItems oli = mapToOrderLineItems(dto);
                    oli.setOrder(order);
                    return oli;
                })
                .toList();

        order.setOrderLineItems(orderLineItemsList);

        List<String> skuCodeList = order.getOrderLineItems().stream()
                .map(OrderLineItems::getSkuCode).toList();

        InventoryResponseDto[] inventoryResponseDtoList;

        try {
            inventoryResponseDtoList = webClient
                    .get()
                    .uri("http://localhost:8082/api/v1/inventory",
                            uriBuilder -> uriBuilder.queryParam("skuCode", skuCodeList).build())
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, clientResponse ->
                            clientResponse.bodyToMono(String.class)
                                    .flatMap(errorBody -> {
                                        return Mono.error(new RuntimeException("Inventory Service Error: " + errorBody));
                                    })
                    )
                    .bodyToMono(InventoryResponseDto[].class)
                    .block();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to check inventory: " + e.getMessage());
        }

        boolean allProductsInStock = Arrays.stream(inventoryResponseDtoList)
                .allMatch(InventoryResponseDto::isInStock);

        if (!allProductsInStock) {
            throw new IllegalArgumentException("One or more products are not in stock");
        }

        Order savedOrder = orderRepository.save(order);

        List<OrderLineItemsDto> savedOrderLineItemsDtoList = savedOrder.getOrderLineItems().stream()
                .map(this::mapToOrderLineItems)
                .toList();

        return new OrderResponse(
                savedOrder.getId(),
                savedOrder.getOrderNumber(),
                savedOrderLineItemsDtoList
        );



    }



    private OrderLineItems mapToOrderLineItems(OrderLineItemsDto orderLineItemsDto){
//        OrderLineItems orderLineItems = new OrderLineItems();
//        orderLineItems.setQuantity(orderLineItemsDto.quantity());
//        orderLineItems.setPrice(orderLineItemsDto.price());
//        orderLineItems.setSkuCode(orderLineItemsDto.skuCode());
//        return orderLineItems;
        return OrderLineItems.builder()
                .quantity(orderLineItemsDto.quantity())
                .price(orderLineItemsDto.price())
                .skuCode(orderLineItemsDto.skuCode())
                .build();
    }

    private OrderLineItemsDto mapToOrderLineItems(OrderLineItems orderLineItems){
        return new  OrderLineItemsDto(orderLineItems.getSkuCode(), orderLineItems.getPrice(), orderLineItems.getQuantity());
    }

}
