package com.shopify.order.service;

import com.shopify.order.dto.OrderRequest;
import com.shopify.order.dto.OrderResponse;

public interface OrderService {

    OrderResponse createOrder(OrderRequest orderRequest);
}
