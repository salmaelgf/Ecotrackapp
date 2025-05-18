package com.example.ecotrack;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Product {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("brand")
    private String brand;

    @SerializedName("carbonFootprint")
    private Double carbonFootprint;



    @SerializedName("barcode")
    private String barcode;

    @SerializedName("alternatives")
    private String[] alternatives;


    @SerializedName("nutrition_score_fr_100g")
    private Double nutritionScore;

    @SerializedName("categories_tags")
    private List<String> categoriesTags;

    public List<String> getCategoriesTags() {
        return categoriesTags;
    }


    public Double getNutritionScore() {
        return nutritionScore;
    }





    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }


    public Double getCarbonFootprint() {
        return carbonFootprint;
    }

    public void setCarbonFootprint(Double carbonFootprint) {
        this.carbonFootprint = carbonFootprint;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String[] getAlternatives() {
        return alternatives;
    }

    public void setAlternatives(String[] alternatives) {
        this.alternatives = alternatives;
    }

}
