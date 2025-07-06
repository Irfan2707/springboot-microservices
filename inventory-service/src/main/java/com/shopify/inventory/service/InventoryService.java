package com.shopify.inventory.service;

import com.shopify.inventory.commons.dto.InventoryResponseDto;

import java.util.List;

public interface InventoryService {

    boolean isInStock(String skuCode);

    List<InventoryResponseDto>  isInStock(List<String> skuCode);
}
