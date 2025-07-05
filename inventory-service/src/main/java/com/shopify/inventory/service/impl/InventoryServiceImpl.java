package com.shopify.inventory.service.impl;

import com.shopify.inventory.repository.InventoryRepository;
import com.shopify.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public boolean isInStock(String skuCode) {
        return inventoryRepository.findBySkuCode(skuCode)
                .map(inv -> inv.getQuantity() > 0)
                .orElse(false);
    }
}
