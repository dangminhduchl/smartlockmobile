package com.example.smartlockjava.ui.historyManagement;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartlockjava.R;
import com.example.smartlockjava.dto.HistoryDeviceResponse;
import com.example.smartlockjava.dto.Result;
import com.example.smartlockjava.preferences.Preferences;

import java.util.List;

public class HistoryFragement extends Fragment {

    private HistoryViewModel historyViewModel;
    private RecyclerView recyclerView;
    private HistoryAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Preferences preferences = new Preferences(getContext());
        String token = preferences.getAccessToken();
        View root = inflater.inflate(R.layout.fragment_history, container, false);

        recyclerView = root.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
        // You need to pass the token here
        historyViewModel.getHistoryDevice("Bearer " + token, null, null, null, null);
        historyViewModel.getHistoryDeviceResponse().observe(getViewLifecycleOwner(), new Observer<HistoryDeviceResponse>() {
            @Override
            public void onChanged(HistoryDeviceResponse historyDeviceResponse) {
                List<Result> historyList = historyDeviceResponse.getResults();
                adapter = new HistoryAdapter(historyList);
                recyclerView.setAdapter(adapter);
            }
        });

        return root;
    }
}