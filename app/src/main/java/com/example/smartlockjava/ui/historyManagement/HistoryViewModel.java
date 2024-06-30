package com.example.smartlockjava.ui.historyManagement;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.smartlockjava.client.ApiClient;
import com.example.smartlockjava.dto.HistoryDeviceResponse;
import com.example.smartlockjava.service.LockControlApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryViewModel extends ViewModel {
    private final MutableLiveData<HistoryDeviceResponse> historyDeviceResponse = new MutableLiveData<>();
    private final LockControlApiService lockControlApiService;

    public HistoryViewModel() {
        lockControlApiService = ApiClient.getClient().create(LockControlApiService.class);
    }

    public LiveData<HistoryDeviceResponse> getHistoryDeviceResponse() {
        return historyDeviceResponse;
    }

    public void getHistoryDevice(String token, String sortBy, String orderBy, String id, String requestLock) {
        Call<HistoryDeviceResponse> call = lockControlApiService.getHistoryDevice(token, sortBy, orderBy, id, requestLock);
        call.enqueue(new Callback<HistoryDeviceResponse>() {
            @Override
            public void onResponse(Call<HistoryDeviceResponse> call, Response<HistoryDeviceResponse> response) {
                if (response.isSuccessful()) {
                    historyDeviceResponse.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<HistoryDeviceResponse> call, Throwable t) {
                // Handle the failure
            }
        });
    }
}