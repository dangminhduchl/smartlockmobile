package com.example.smartlockjava.service;

import android.widget.Toast;

import com.example.smartlockjava.client.ApiClient;
import com.example.smartlockjava.dto.LockControlRequest;
import com.example.smartlockjava.dto.LockControlResponse;
import com.example.smartlockjava.dto.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LockControlViewModel {
    private final LockControlApiService lockControlApiService;

    public LockControlViewModel() {
        lockControlApiService = ApiClient.getClient().create(LockControlApiService.class);
    }

    public void lockControl(String token, boolean lock, Callback<LockControlResponse> callback) {
        LockControlRequest lockControlRequest = new LockControlRequest(lock);
        Call<LockControlResponse> call = lockControlApiService.lockControl(String.format("Bearer %s", token), lockControlRequest);
        call.enqueue(callback);
    }
}
