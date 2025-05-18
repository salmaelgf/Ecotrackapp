package com.example.ecotrack;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {

    @POST("/api/auth/signup")
    Call<String> signUp(@Body UserRequest user);

    @POST("/api/auth/signin")
    Call<String> signIn(@Body UserRequest user);
}
