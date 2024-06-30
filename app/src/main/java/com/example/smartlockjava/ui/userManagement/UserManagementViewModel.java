package com.example.smartlockjava.ui.userManagement;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.smartlockjava.client.ApiClient;
import com.example.smartlockjava.dto.ChangePasswordRequest;
import com.example.smartlockjava.dto.PersonSync;
import com.example.smartlockjava.dto.UpdateUserRequest;
import com.example.smartlockjava.dto.Person;
import com.example.smartlockjava.service.UserService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class UserManagementViewModel extends ViewModel {
    private final MutableLiveData<List<Person>> users;
    private final UserService userService;

    public UserManagementViewModel() {
        users = new MutableLiveData<>();
        userService = ApiClient.getClient().create(UserService.class);
    }

    public LiveData<List<Person>> getUsers() {
        return users;
    }

    public void loadUsers(String authHeader, Callback<List<Person>> callback) {
        Call<List<Person>> call = userService.getUsers(authHeader);
        call.enqueue(callback);
    }

    public void syncUser(String authHeader, Callback<List<PersonSync>> callback) {
        Call<List<PersonSync>> call = userService.syncUser(authHeader);
        call.enqueue(callback);
    }

    public void setUsers(List<Person> userList) {
        users.postValue(userList);
    }

    public void updateUser(String authHeader, int userId, UpdateUserRequest updateUserRequest, Callback<Person> callback) {
        Call<Person> call = userService.updateUser(authHeader, userId, updateUserRequest);
        call.enqueue(callback);
    }

    public void changePassword(String authHeader, int userId, ChangePasswordRequest changePasswordRequest, Callback<Person> callback) {
        Call<Person> call = userService.changePassword(authHeader, userId, changePasswordRequest);
        call.enqueue(callback);
    }
}