package com.example.smartlockjava.ui.userManagement;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartlockjava.R;
import com.example.smartlockjava.dto.Person;
import com.example.smartlockjava.preferences.Preferences;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserManagementFragment extends Fragment {
    private UserManagementViewModel viewModel;
    private RecyclerView recyclerView;
    private UserAdapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Preferences preferences = new Preferences(getContext());
        String token = preferences.getAccessToken();
        View root = inflater.inflate(R.layout.fragment_user_management, container, false);
        recyclerView = root.findViewById(R.id.user_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel = new ViewModelProvider(this).get(UserManagementViewModel.class);
        viewModel.getUsers().observe(getViewLifecycleOwner(), users -> {
            adapter = new UserAdapter(getContext(), users, "Bearer " + token);
            recyclerView.setAdapter(adapter);
        });

        viewModel.loadUsers("Bearer " + token, new Callback<List<Person>>() {
            @Override
            public void onResponse(Call<List<Person>> call, Response<List<Person>> response) {
                if (response.isSuccessful()) {
                    viewModel.setUsers(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Person>> call, Throwable t) {
                t.printStackTrace();

            }
        });

        return root;
    }
}