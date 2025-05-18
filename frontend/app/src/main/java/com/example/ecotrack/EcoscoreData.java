package com.example.ecotrack;

import com.google.gson.annotations.SerializedName;

public class EcoscoreData {

        @SerializedName("agribalyse")
        private Agribalyse agribalyse;

    public Agribalyse getAgribalyse() {
        return agribalyse;
    }

    public void setAgribalyse(Agribalyse agribalyse) {
        this.agribalyse = agribalyse;
    }
}

