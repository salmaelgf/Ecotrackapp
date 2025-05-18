package com.example.ecotrack;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;

import retrofit2.http.Path;

public interface FlaskApi {

        @GET("/product/{barcode}")
        Call<ProductInput> getProductByBarcode(@Path("barcode") String barcode);
    }


