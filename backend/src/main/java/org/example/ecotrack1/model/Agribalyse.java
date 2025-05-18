package org.example.ecotrack1.model;



import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.fasterxml.jackson.annotation.JsonProperty;

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
