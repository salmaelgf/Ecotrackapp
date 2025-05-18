package com.example.ecotrack;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory; // ⬅️ à importer

public class ApiClient {
    private static Retrofit retrofit;
    private static Retrofit flaskRetrofit;
    private static Retrofit flaskInstance;

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.10.85:8081/")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getFlaskRetrofit() {
        if (flaskRetrofit == null) {
            flaskRetrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.10.85:5000/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return flaskRetrofit;
    }

    public static Retrofit getFlaskInstance() {
        if (flaskInstance == null) {
            flaskInstance = new Retrofit.Builder()
                    .baseUrl("http://192.168.10.85:5000/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return flaskInstance;
    }
}
