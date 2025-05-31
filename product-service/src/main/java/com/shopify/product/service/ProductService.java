package com.shopify.product.service;

import com.shopify.product.dto.ProductRequest;
import com.shopify.product.dto.ProductResponse;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {

    public ProductResponse createProduct(ProductRequest productRequest);

    List<ProductResponse> getAllProducts();
}
