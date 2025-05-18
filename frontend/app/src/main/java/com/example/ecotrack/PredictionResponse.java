
package com.example.ecotrack;
public class PredictionResponse {
    private String product_name;
    private double predicted_carbon_footprint_100g;
    private String predicted_impact_category;

    // Getters and Setters
    public String getProduct_name() { return product_name; }
    public void setProduct_name(String product_name) { this.product_name = product_name; }

    public double getPredicted_carbon_footprint_100g() { return predicted_carbon_footprint_100g; }
    public void setPredicted_carbon_footprint_100g(double value) { this.predicted_carbon_footprint_100g = value; }

    public String getPredicted_impact_category() { return predicted_impact_category; }
    public void setPredicted_impact_category(String category) { this.predicted_impact_category = category; }
}
