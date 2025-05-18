package org.example.ecotrack1.controller;

import org.example.ecotrack1.model.Product;
import org.example.ecotrack1.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/scan/{barcode}")
    public ResponseEntity<Product> scanProduct(@PathVariable String barcode) {
        Product product = productService.getProductDetails(barcode);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }
}
