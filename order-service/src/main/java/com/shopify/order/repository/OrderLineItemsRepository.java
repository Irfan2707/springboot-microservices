package com.shopify.order.repository;

import com.shopify.order.model.OrderLineItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.beans.JavaBean;

public interface OrderLineItemsRepository extends JpaRepository<OrderLineItems,Long> {
}
