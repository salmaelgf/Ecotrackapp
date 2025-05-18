package com.example.ecotrack;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface FlaskPredictionApi {

    @GET("predict/{barcode}")
    Call<PredictionResponse> getPrediction(@Path("barcode") String barcode);
}
