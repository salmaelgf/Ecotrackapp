package org.example.ecotrack1.client;

import org.example.ecotrack1.model.SmartAlternativeResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface SmartApi {
    @GET("/api/smart/alternatives/{barcode}")
    Call<SmartAlternativeResponse> getAlternatives(@Path("barcode") String barcode);
}
