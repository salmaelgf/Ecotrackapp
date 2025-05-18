
package com.example.ecotrack;

import com.google.gson.annotations.SerializedName;

public class PredictionRequest {
    private String barcode;

    public PredictionRequest(String barcode) {
        this.barcode = barcode;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
