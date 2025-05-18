package org.example.ecotrack1.controller;

import org.example.ecotrack1.client.OpenFoodFactsClient;
import org.example.ecotrack1.client.SmartAlternativesEngine;
import org.example.ecotrack1.model.Product;
import org.example.ecotrack1.model.SmartAlternativeResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/smart")
public class SmartAlternativeController {

    private final OpenFoodFactsClient productClient;
    private final SmartAlternativesEngine smartEngine;

    public SmartAlternativeController(OpenFoodFactsClient productClient, SmartAlternativesEngine smartEngine) {
        this.productClient = productClient;
        this.smartEngine = smartEngine;
    }

    @GetMapping("/alternatives/{barcode}")
    public SmartAlternativeResponse getAlternatives(@PathVariable String barcode) {
        Product product = productClient.fetchProduct(barcode);

        if (product == null || product.getCarbonFootprint() == null || product.getCarbonFootprint() == 0.0) {
            return new SmartAlternativeResponse("Product not found or no carbon data", List.of());
        }

        String category = "";
        String name = product.getName() != null ? product.getName() : "";
        String brand = product.getBrand() != null ? product.getBrand() : "";

        if (product.getCategoriesTags() != null && !product.getCategoriesTags().isEmpty()) {
            category = product.getCategoriesTags().get(0).replace("en:", "");
        }

        List<String> alternatives = smartEngine.suggestAlternatives(category, name, brand, product.getCarbonFootprint());
        return new SmartAlternativeResponse(product.getName(), alternatives);
    }
}
