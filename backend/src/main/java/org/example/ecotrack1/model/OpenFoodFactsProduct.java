package org.example.ecotrack1.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OpenFoodFactsProduct {

    @SerializedName("product_name")
    private String name;

    @SerializedName("brands")
    private String brand;

    @SerializedName("code")
    private String code;

    @SerializedName("ecoscore_data")
    private EcoscoreData ecoscoreData;
    @SerializedName("categories_tags")
    private List<String> categoriesTags;

    // Getters & Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public EcoscoreData getEcoscoreData() { return ecoscoreData; }
    public void setEcoscoreData(EcoscoreData ecoscoreData) { this.ecoscoreData = ecoscoreData; }
    public List<String> getCategoriesTags() {
        return categoriesTags;
    }

    public void setCategoriesTags(List<String> categoriesTags) {
        this.categoriesTags = categoriesTags;
    }
}
