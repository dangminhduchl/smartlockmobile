package com.example.smartlockjava.service;

import com.example.smartlockjava.dto.LoginResponse;
import com.example.smartlockjava.dto.SignUpResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface LoginApiService {
    @FormUrlEncoded
    @POST("user/login/")
    Call<LoginResponse> login(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("user/register/")
    Call<SignUpResponse> signup(@Field("username") String username, @Field("password") String password, @Field("email") String email, @Field("face_vector") String facevector);

    @FormUrlEncoded
    @POST("user/face-login/")
    Call<LoginResponse> faceLogin(@Field("username") String username);

}