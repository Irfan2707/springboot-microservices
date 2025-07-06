package com.shopify.inventory.service.impl;

import com.shopify.inventory.commons.dto.InventoryResponseDto;
import com.shopify.inventory.repository.InventoryRepository;
import com.shopify.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Transactional(readOnly = true)
    @Override
    public List<InventoryResponseDto> isInStock(List<String> stringList) {

       List<InventoryResponseDto> inventoryResponseDtoList = inventoryRepository.findBySkuCodeIn(stringList)
                .stream()
                .map(inv -> InventoryResponseDto.builder().skuCode(inv.getSkuCode())
                        .inStock(inv.getQuantity() > 0)
                        .build())
                        .toList();
        if (!inventoryResponseDtoList.isEmpty()) {
            return inventoryResponseDtoList;
        }else {
            throw new RuntimeException("No inventory found for the provided SKU codes: " + stringList);
        }
    }
}
