package com.shoppulse.product.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shoppulse.product.entity.ProductEntity;
import com.shoppulse.product.entity.Review;
import com.shoppulse.product.models.CreateProductRequest;
import com.shoppulse.product.models.ProductView;
import com.shoppulse.product.repository.ProductRepository;
import com.shoppulse.product.service.ProductService;

import lombok.extern.slf4j.Slf4j;

import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService{

    private final ProductRepository repository ;


    @Autowired
    public ProductServiceImpl(ProductRepository repository) {
        this.repository = repository ;
    }

    @Transactional
    public ProductView createProduct(CreateProductRequest request){
        ProductEntity newProduct = new ProductEntity() ;
        newProduct.setSku(request.sku());
        newProduct.setName(request.name());
        newProduct.setCategory(request.category());
        newProduct.setPrice(request.price());
        newProduct.setDescription(request.name()+request.category());
        newProduct.setReviewList(null);
        ProductEntity savedProductEntity = repository.save(newProduct) ;
        return new ProductView(
            savedProductEntity.getId(),
            savedProductEntity.getSku(),
            savedProductEntity.getName(),
            savedProductEntity.getPrice(),
            savedProductEntity.getCategory(),
            savedProductEntity.getReviewList().stream().map(Review::getComment).toList()
        );
    }
    
    @Cacheable(cacheNames="products", key = "#id")
    public Optional<ProductView> getById(Long id){
        log.info("Cache MISS for product id {}", id);
        return repository.findById(id).map(product -> 
            new ProductView(
                product.getId(), 
                product.getSku(),
                product.getName(), 
                product.getPrice(), 
                product.getCategory() != null ? product.getCategory() : "UnCategorized",
                product.getReviewList().stream().map(Review::getComment).toList())
            ) ;
    }

    public List<ProductEntity> findAllMatching(Predicate<ProductEntity> predicate){
        return repository.findAll()
        .stream()
        .filter(predicate)
        .collect(Collectors.toList()) ;
    }

    public List<ProductView> findAllProductWithReviews(){
        List<ProductEntity> products = repository.findAll();
        return products.stream().map(product -> new ProductView(
            product.getId(),
            product.getSku(),
            product.getName(),
            product.getPrice(),
            product.getCategory(),
            product.getReviewList().stream().map(Review::getComment).toList()
        )).toList() ;
    }
}
