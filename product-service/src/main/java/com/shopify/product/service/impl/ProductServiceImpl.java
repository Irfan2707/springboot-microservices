package com.shopify.product.service.impl;

import com.shopify.product.dto.ProductRequest;
import com.shopify.product.dto.ProductResponse;
import com.shopify.product.model.Product;
import com.shopify.product.repository.ProductRepository;
import com.shopify.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {

        Product product = Product.builder()
                .name(productRequest.name())
                .description(productRequest.description())
                .price(productRequest.price())
                .build();
        productRepository.save(product);

        log.info("Product created successfully: {}", product);

        return new ProductResponse(product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice());

    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(product -> new ProductResponse(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice()))
                .toList();

    }

}
