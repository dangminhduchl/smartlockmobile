package com.example.smartlockjava.service;

import com.example.smartlockjava.dto.HistoryDeviceResponse;
import com.example.smartlockjava.dto.LockControlRequest;
import com.example.smartlockjava.dto.LockControlResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LockControlApiService {
    @POST("device/control/")
    Call<LockControlResponse> lockControl(@Header("Authorization") String token, @Body LockControlRequest lockControlRequest);

    @GET("device/history")
        // Replace with your actual endpoint
    Call<HistoryDeviceResponse> getHistoryDevice(
            @Header("Authorization") String token,
            @Query("sort_by") String sortBy,
            @Query("order_by") String orderBy,
            @Query("id") String id,
            @Query("request_lock") String requestLock
    );
}
