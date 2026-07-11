package com.shoppulse.product.repository;

import com.shoppulse.product.entity.ProductEntity;
import com.shoppulse.product.entity.Review;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductRepository extends JpaRepository<ProductEntity,Long>{
    // Optional<ProductEntity> findBySku(String sku);
    
    @EntityGraph(attributePaths = {"reviewList"})
    List<ProductEntity> findAll();
}
