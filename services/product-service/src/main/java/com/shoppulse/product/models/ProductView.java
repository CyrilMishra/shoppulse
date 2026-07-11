package com.shoppulse.product.models;

import java.io.Serializable;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public record ProductView(Long id, String sku, String name, Double price, String category, List<String> reviewComments) implements Serializable {

}

