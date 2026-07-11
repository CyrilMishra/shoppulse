package com.shoppulse.product.service;

import java.util.List;
import java.util.Optional;

import com.shoppulse.product.models.CreateProductRequest;
import com.shoppulse.product.models.ProductView;

public interface ProductService {
    Optional<ProductView> getById(Long id) ;
    public ProductView createProduct(CreateProductRequest request) ;
    public List<ProductView> findAllProductWithReviews();
}
