package com.example.smartlockjava.service;

import android.content.Context;

import com.example.smartlockjava.client.ApiClient;
import com.example.smartlockjava.dto.SignUpResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpViewModel {
    private final LoginApiService loginApiService;

    public SignUpViewModel(Context context) {
        loginApiService = ApiClient.getClient().create(LoginApiService.class);
    }

    public void signup(String username, String password, String email, String faceVector, final SignupCallback callback) {
        Call<SignUpResponse> call = loginApiService.signup(username, password, email, faceVector);
        call.enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                SignUpResponse signUpResponse = response.body();
                if (response.isSuccessful()) {
                    callback.onSuccess("Signup Success");
                } else {
                    callback.onError("Signup failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                callback.onError("Login failed: " + t.getMessage());
            }
        });
    }

    public interface SignupCallback {
        void onSuccess(String response);

        void onError(String error);
    }
}


