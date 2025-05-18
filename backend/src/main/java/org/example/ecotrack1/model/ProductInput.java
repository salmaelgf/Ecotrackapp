package org.example.ecotrack1.model;

public class ProductInput {
    private double nutrition_score;
    private String category;

    // Constructors
    public ProductInput() {}

    public ProductInput(double nutrition_score, String category) {
        this.nutrition_score = nutrition_score;
        this.category = category;
    }

    // Getters and Setters
    public double getNutrition_score() {
        return nutrition_score;
    }

    public void setNutrition_score(double nutrition_score) {
        this.nutrition_score = nutrition_score;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
