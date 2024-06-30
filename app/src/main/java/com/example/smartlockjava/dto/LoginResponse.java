package com.example.smartlockjava.dto;

import com.example.smartlockjava.manager.JWTDecoder;

public class LoginResponse {

    private String access;
    private String refresh;
    private String userId;
    private String userName;

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getRefresh() {
        return refresh;
    }

    public void setRefresh(String refresh) {
        this.refresh = refresh;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserId(String access_token) {
        this.userId = JWTDecoder.decodeJwt(access_token).get("user_id").asString();
    }

    public void setUserName(String access_token) {
        this.userName = JWTDecoder.decodeJwt(access_token).get("name").asString();
    }
}