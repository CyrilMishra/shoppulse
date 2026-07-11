package com.shoppulse.product.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shoppulse.product.models.CreateProductRequest;
import com.shoppulse.product.models.ProductView;
import com.shoppulse.product.service.ProductService;
import com.shoppulse.product.service.impl.ProductServiceImpl;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("api/products")
public class ProductController {
    private final ProductService productService ;

    @Autowired
    public ProductController(ProductService productService){
        this.productService = productService ;
    }

    @Operation(summary = "Get Products By ID", description = "To get Product pass the ID as well.")
    @GetMapping("/{id}")
    public ResponseEntity<ProductView> getProductById(@PathVariable Long id){
        return productService.getById(id)
        .map(productView -> ResponseEntity.ok(productView))
        .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get All Products", description = "To get all Products with Review.")
    @GetMapping
    public ResponseEntity<List<ProductView>> getProductsWithReview(){
        List<ProductView> products = productService.findAllProductWithReviews();
        return ResponseEntity.ok(products) ;
    }
    
    @Operation(summary = "Create new Product", description = "To create new Product with new ID.")
    @PostMapping
    public ResponseEntity<ProductView> saveProducEntity(@RequestBody CreateProductRequest request){
        ProductView createdProduct = productService.createProduct(request) ;
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct) ;
    }
    
}
