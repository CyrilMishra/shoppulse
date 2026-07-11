package com.shoppulse.product.models;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public record CreateProductRequest(String sku,String name, Double price, String category ) {
    
}
