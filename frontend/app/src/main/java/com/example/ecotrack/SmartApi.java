package com.example.ecotrack;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface SmartApi {
    @GET("api/smart/alternatives/{barcode}")
    Call<SmartAlternativeResponse> getAlternatives(@Path("barcode") String barcode);

}
