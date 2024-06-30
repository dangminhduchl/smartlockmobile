package com.example.smartlockjava.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PersonSync implements Serializable {
    private String username;
    @SerializedName("face_vector")
    private String faceVector;

    public PersonSync(int id, String username, String first_name, String last_name, String email, boolean superuser, boolean is_staff, boolean is_active, boolean encode) {
        this.username = username;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getFaceVector() {
        return faceVector;
    }

    public void setFace_vector(float[] face_vector) {
        this.faceVector = faceVector;
    }
}
