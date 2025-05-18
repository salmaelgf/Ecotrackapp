package com.example.ecotrack;

import com.google.gson.annotations.SerializedName;

public class Agribalyse {
    @SerializedName("co2_total")
    private Double co2Total;

    public Double getCo2Total() {
        return co2Total;
    }

    public void setCo2Total(Double co2Total) {
        this.co2Total = co2Total;
    }

}
