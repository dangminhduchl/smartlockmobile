package com.example.smartlockjava.service;

import com.example.smartlockjava.dto.ChangePasswordRequest;
import com.example.smartlockjava.dto.PersonSync;
import com.example.smartlockjava.dto.UpdateUserRequest;
import com.example.smartlockjava.dto.Person;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {
    @GET("user/users")
    Call<List<Person>> getUsers(@Header("Authorization") String authHeader);

    @GET("user/users")
    Call<List<PersonSync>> syncUser(@Header("Authorization") String authHeader);

    @PUT("user/user/{id}/")
    Call<Person> updateUser(@Header("Authorization") String authHeader, @Path("id") int id, @Body UpdateUserRequest updateUserRequest);

    @POST("user/user/{id}/")
    Call<Person> changePassword(@Header("Authorization") String authHeader, @Path("id") int id, @Body ChangePasswordRequest changePasswordRequest);
}
