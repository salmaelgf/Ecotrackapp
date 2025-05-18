package org.example.ecotrack1.model;


import java.util.List;

public class SmartAlternativeResponse {

    private String originalProduct;
    private List<String> alternatives;

    public SmartAlternativeResponse() {}

    public SmartAlternativeResponse(String originalProduct, List<String> alternatives) {
        this.originalProduct = originalProduct;
        this.alternatives = alternatives;
    }

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
