package com.example.smartlockjava.service;

import android.content.Context;

import com.example.smartlockjava.client.ApiClient;
import com.example.smartlockjava.dto.LoginResponse;
import com.example.smartlockjava.manager.JWTDecoder;
import com.example.smartlockjava.preferences.Preferences;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel {
    private JWTDecoder jwtDecoder;
    private final LoginApiService loginApiService;
    private final Preferences preferences;

    public LoginViewModel(Context context) {
        loginApiService = ApiClient.getClient().create(LoginApiService.class);
        preferences = new Preferences(context);
    }

    public void login(String username, String password, final LoginCallback callback) {
        Call<LoginResponse> call = loginApiService.login(username, password);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse != null && loginResponse.getAccess() != null) {
                        // Save token here
                        preferences.saveAccessToken(loginResponse.getAccess());
                        preferences.saveRefreshToken(loginResponse.getRefresh());
                        loginResponse.setUserId(loginResponse.getAccess());
                        loginResponse.setUserName(loginResponse.getAccess());
                        preferences.saveUserId(loginResponse.getUserId());
                        preferences.saveUserName(loginResponse.getUserName());
                        callback.onSuccess(loginResponse);
                    } else {
                        callback.onError("Token not found in response");
                    }
                } else {
                    callback.onError("Login failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                callback.onError("Login failed: " + t.getMessage());
            }
        });
    }

    public void faceLogin(String username, final LoginCallback callback) {
        Call<LoginResponse> call = loginApiService.faceLogin(username);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse != null && loginResponse.getAccess() != null) {
                        // Save token here
                        preferences.saveAccessToken(loginResponse.getAccess());
                        preferences.saveRefreshToken(loginResponse.getRefresh());
                        loginResponse.setUserId(loginResponse.getAccess());
                        loginResponse.setUserName(loginResponse.getAccess());
                        preferences.saveUserId(loginResponse.getUserId());
                        preferences.saveUserName(loginResponse.getUserName());
                        callback.onSuccess(loginResponse);
                    } else {
                        callback.onError("Token not found in response");
                    }
                } else {
                    callback.onError("Login failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                callback.onError("Login failed: " + t.getMessage());
            }
        });
    }

    public interface LoginCallback {
        void onSuccess(LoginResponse response);

        void onError(String error);
    }
}


