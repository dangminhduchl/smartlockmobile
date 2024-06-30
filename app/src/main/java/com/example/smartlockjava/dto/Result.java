package com.example.smartlockjava.dto;

public class Result {
    private int id;
    private LockRequest request;
    private String user_name;
    private String created_at;
    private int user;

    // getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LockRequest getRequest() {
        return request;
    }

    public void setRequest(LockRequest request) {
        this.request = request;
    }


    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }
}
