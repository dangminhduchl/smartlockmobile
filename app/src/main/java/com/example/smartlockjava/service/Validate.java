package com.example.smartlockjava.service;

public class Validate {
    public static boolean validateUsername(String username) {
        return username != null && username.length() >= 3 && username.length() <= 20;
    }

    public static boolean validatePassword(String password) {
        if (password != null && password.length() >= 8 && password.length() <= 20) {
            return password.matches(".*[0-9].*") && password.matches(".*[a-z].*") && password.matches(".*[A-Z].*");

        }
        return false;
    }

    public static boolean validateName(String name) {
        return name != null && name.length() >= 3 && name.length() <= 20;
    }
}
