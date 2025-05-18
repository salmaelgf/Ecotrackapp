package org.example.ecotrack1.service;



import org.example.ecotrack1.model.Product;
import org.example.ecotrack1.client.OpenFoodFactsClient;
import org.example.ecotrack1.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final OpenFoodFactsClient openFoodFactsClient;
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(OpenFoodFactsClient openFoodFactsClient, ProductRepository productRepository) {
        this.openFoodFactsClient = openFoodFactsClient;
        this.productRepository = productRepository;
    }

    public Product getProductDetails(String barcode) {
        List<Product> products = productRepository.findByBarcode(barcode);
        if (!products.isEmpty()) {
            return products.get(0);
        }

        Product product = openFoodFactsClient.fetchProduct(barcode);
        if (product != null) {
            productRepository.save(product);
        }

        return product;
    }

}
