package org.example.ecotrack1.client;

import org.example.ecotrack1.model.ProductResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface OpenFoodFactsApi {
    @GET("product/{barcode}.json")
    Call<ProductResponse> getProductDetails(@Path("barcode") String barcode);
}
