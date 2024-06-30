package com.example.smartlockjava.service;

import com.example.smartlockjava.client.ApiClient;
import com.example.smartlockjava.dto.Person;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class UserManagementViewModel {
    private final UserService userService;


    public UserManagementViewModel() {
        userService = ApiClient.getClient().create(UserService.class);
    }

    public void getUser(String token, Callback<List<Person>> callback) {
        Call<List<Person>> call = userService.getUsers(String.format("Bearer %s", token));
        call.enqueue(callback);
    }
}
