package com.shopify.order.dto;

import java.math.BigDecimal;
import java.util.List;

public record OrderResponse(Long orderid, String orderNumber, List<OrderLineItemsDto> orderLineItemsDtoList) {

}
