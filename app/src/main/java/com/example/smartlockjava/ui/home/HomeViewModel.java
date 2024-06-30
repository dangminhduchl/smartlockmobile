package com.example.smartlockjava.ui.home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.smartlockjava.client.WebSocketClient;

import org.json.JSONObject;

public class HomeViewModel extends ViewModel {
    private final MutableLiveData<String> mText;
    private final MutableLiveData<String> lockStatus;
    private final MutableLiveData<String> doorStatus;
    private final WebSocketClient webSocketClient;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");

        lockStatus = new MutableLiveData<>();
        doorStatus = new MutableLiveData<>();

        webSocketClient = new WebSocketClient() {
            @Override
            public void onMessage(String text) {
                try {
                    Log.d("socket", text);
                    JSONObject jsonObject = new JSONObject(text);
                    lockStatus.postValue(jsonObject.getString("lock"));
                    doorStatus.postValue(jsonObject.getString("door"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        webSocketClient.start();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<String> getLockStatus() {
        return lockStatus;
    }

    public LiveData<String> getDoorStatus() {
        return doorStatus;
    }

    @Override
    protected void onCleared() {
        webSocketClient.close();
        super.onCleared();
    }
}