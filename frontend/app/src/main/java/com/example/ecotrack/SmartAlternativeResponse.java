package com.example.ecotrack;


import java.util.List;

public class SmartAlternativeResponse {
    private String originalProduct;
    private List<String> alternatives;

    public String getOriginalProduct() {
        return originalProduct;
    }

    public void setOriginalProduct(String originalProduct) {
        this.originalProduct = originalProduct;
    }

    public List<String> getAlternatives() {
        return alternatives;
    }

    public void setAlternatives(List<String> alternatives) {
        this.alternatives = alternatives;
    }
}
