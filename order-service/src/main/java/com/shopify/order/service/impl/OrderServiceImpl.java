package com.shopify.order.service.impl;

import com.shopify.order.dto.OrderLineItemsDto;
import com.shopify.order.dto.OrderRequest;
import com.shopify.order.dto.OrderResponse;
import com.shopify.order.model.Order;
import com.shopify.order.model.OrderLineItems;
import com.shopify.order.repository.OrderRepository;
import com.shopify.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private  final OrderRepository orderRepository;


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

        // Save order (with cascade = ALL, line items are saved too)
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
