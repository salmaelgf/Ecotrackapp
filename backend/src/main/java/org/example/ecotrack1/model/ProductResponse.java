package org.example.ecotrack1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class ProductResponse {
    @JsonProperty("product")
    @SerializedName("product")
    private OpenFoodFactsProduct product;

    public OpenFoodFactsProduct getProduct() {
        return product;
    }

    public void setProduct(OpenFoodFactsProduct product) {
        this.product = product;
    }
}
