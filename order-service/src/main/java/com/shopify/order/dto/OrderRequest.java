package com.shopify.order.dto;


import java.util.List;

public record OrderRequest(List<OrderLineItemsDto> orderLineItemsDtoList) {

}
