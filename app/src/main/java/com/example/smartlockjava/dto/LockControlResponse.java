package com.example.smartlockjava.dto;

public class LockControlResponse {
    public LockControlResponse(String message, String lock, String door, String error) {
        this.message = message;
        this.lock = lock;
        this.door = door;
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLock() {
        return lock;
    }

    public void setLock(String lock) {
        this.lock = lock;
    }

    public String getDoor() {
        return door;
    }

    public void setDoor(String door) {
        this.door = door;
    }

    public String getError() {
        return error;
    }

    String message;
    String lock;
    String door;
    String error;

}
