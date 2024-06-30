package com.example.smartlockjava.dto;

import com.google.gson.annotations.SerializedName;

public class UpdateUserRequest {
    @SerializedName("is_active")
    boolean active;
    @SerializedName("is_superuser")
    boolean superuser;

    public UpdateUserRequest(boolean is_active, boolean is_staff) {
        this.active = is_active;
        this.superuser = is_staff;
    }
}
