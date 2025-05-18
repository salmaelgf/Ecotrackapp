package com.example.ecotrack;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;
    private static Retrofit flaskRetrofit;
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.10.85:8081/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getFlaskInstance() {
        if (flaskRetrofit == null) {
            flaskRetrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.10.85:5000/") // Flask en local (émulateur Android)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return flaskRetrofit;
    }
}
