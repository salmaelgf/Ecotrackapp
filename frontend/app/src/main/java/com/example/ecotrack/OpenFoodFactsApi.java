package com.example.ecotrack;



import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface OpenFoodFactsApi {
    @GET("api/products/scan/{barcode}")
    Call<Product> getProductDetails(@Path("barcode") String barcode);
}
