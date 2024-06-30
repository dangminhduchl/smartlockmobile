package com.example.smartlockjava.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Person implements Serializable {
    private int id;
    private String username;
    private String first_name;
    private String last_name;
    private String email;
    @SerializedName("is_superuser")
    private boolean superuser;
    @SerializedName("is_staff")
    private boolean staff;
    @SerializedName("is_active")
    private boolean active;
    private boolean encode;
    private String faceVector;

    public Person(int id, String username, String first_name, String last_name, String email, boolean superuser, boolean is_staff, boolean is_active, boolean encode) {
        this.id = id;
        this.username = username;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.staff = is_staff;
        this.active = is_active;
        this.encode = encode;
        this.superuser = superuser;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isStaff() {
        return staff;
    }

    public void setStaff(boolean is_staff) {
        this.staff = is_staff;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean is_active) {
        this.active = is_active;
    }

    public boolean isEncode() {
        return encode;
    }

    public void setEncode(boolean encode) {
        this.encode = encode;
    }

    public boolean isSuperuser() {
        return superuser;
    }

    public void setSuperuser(boolean superuser) {
        this.superuser = superuser;
    }

    public String getFaceVector() {
        return faceVector;
    }

    public void setFace_vector(float[] face_vector) {
        this.faceVector = faceVector;
    }
}
