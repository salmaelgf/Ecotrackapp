package org.example.ecotrack1.model;

public class PredictionResponse {
    private double carbon_footprint;

    public PredictionResponse(double carbon_footprint) {
        this.carbon_footprint = carbon_footprint;
    }

    public double getCarbon_footprint() {
        return carbon_footprint;
    }

    public void setCarbon_footprint(double carbon_footprint) {
        this.carbon_footprint = carbon_footprint;
    }
}
