package com.example.smartlockjava.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    private SharedPreferences sharedPreferences;
    private static String AUTH_PREFS_NAME = "auth_prefs";
    private static String KEY_REFRESH_TOKEN = "refresh_token";
    private static String KEY_ACCESS_TOKEN = "access_token";
    private static String KEY_USER_ID = "user_id";
    private static String KEY_USER_NAME = "user_name";

    public Preferences(Context context) {
        sharedPreferences = context.getSharedPreferences(AUTH_PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveRefreshToken(String refreshToken) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_REFRESH_TOKEN, refreshToken);
        editor.apply();
    }

    public void saveAccessToken(String accessToken) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ACCESS_TOKEN, accessToken);
        editor.apply();
    }

    public void saveUserId(String userId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_ID, userId);
        editor.apply();
    }

    public void saveUserName(String userName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_NAME, userName);
        editor.apply();
    }

    public String getRefreshToken() {
        return sharedPreferences.getString(KEY_REFRESH_TOKEN, null);
    }

    public String getAccessToken() {
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null);
    }

    public String getUserId() {
        return sharedPreferences.getString(KEY_USER_ID, null);
    }

    public String getUserName() {
        return sharedPreferences.getString(KEY_USER_NAME, null);
    }

    public void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}