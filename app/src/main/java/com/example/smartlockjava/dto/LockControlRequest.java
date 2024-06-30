package com.example.smartlockjava.dto;

public class LockControlRequest {
    String lock;

    public LockControlRequest(boolean lock) {
        this.lock = "0";
        if (lock) {
            this.lock = "1";
        }
    }

    public String getLock() {
        return lock;
    }

    public String setLock(boolean lock) {
        if (lock) {
            return "1";
        }
        return "0";
    }
}
