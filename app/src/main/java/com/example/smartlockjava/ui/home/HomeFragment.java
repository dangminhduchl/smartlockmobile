package com.example.smartlockjava.ui.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.smartlockjava.FaceLoginActivity;
import com.example.smartlockjava.R;
import com.example.smartlockjava.databinding.FragmentHomeBinding;
import com.example.smartlockjava.dto.LockControlResponse;
import com.example.smartlockjava.preferences.Preferences;
import com.example.smartlockjava.service.LockControlViewModel;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private boolean lockControl = false;

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final ImageView doorStatusImageView = binding.imageDoorStatus;
        final ImageView lockStatusImageView = binding.imageLockStatus;
        final Button lockControlButton = binding.lockControlButton;

        homeViewModel.getDoorStatus().observe(getViewLifecycleOwner(), doorStatus -> {
            if (Objects.equals(doorStatus, "0")) {
                doorStatusImageView.setImageResource(R.drawable.open_door);
            } else {
                doorStatusImageView.setImageResource(R.drawable.close_door);
            }
        });

        homeViewModel.getLockStatus().observe(getViewLifecycleOwner(), lockStatus -> {
            if (Objects.equals(lockStatus, "0")) {
                lockStatusImageView.setImageResource(R.drawable.open_lock);
                lockControlButton.setText("Lock");
                lockControl = true;
            } else {
                lockStatusImageView.setImageResource(R.drawable.close_lock);
                lockControlButton.setText("Unlock");
                lockControl = false;
            }

        });

        lockControlButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                Preferences preferences = new Preferences(getContext());
                String token = preferences.getAccessToken();
                if (token == null) {
                    Intent intent = new Intent(getContext(), FaceLoginActivity.class);
                    startActivityForResult(intent, 1); // Start the FaceLoginActivity and expect a result
                } else {
                    sendLockControlRequest(token, lockControl);
                }
            }
        });
        return root;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String token = data.getStringExtra("token");
                sendLockControlRequest(token, lockControl);
            }
        }
    }

    private void sendLockControlRequest(String token, boolean lockControl) {
        LockControlViewModel lockControlViewModel = new LockControlViewModel();
        lockControlViewModel.lockControl(token, lockControl, new Callback<LockControlResponse>() {
            @Override
            public void onResponse(Call<LockControlResponse> call, Response<LockControlResponse> response) {
                if (response.isSuccessful()) {
                    LockControlResponse lockControlResponse = response.body();
                    if (lockControlResponse != null) {
                        Toast.makeText(getContext(), lockControlResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Lock control response is null", Toast.LENGTH_SHORT).show();
                    }
                } else if (response.code() == 501) {
                    try {
                        Toast.makeText(getContext(), response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Toast.makeText(getContext(), "Lock control failed: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LockControlResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Lock control failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}