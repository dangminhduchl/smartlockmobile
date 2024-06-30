package com.example.smartlockjava.dto;

public class LockRequest {
    private int id;
    private boolean lock;
    private boolean door;
    private String update_at;

    // getters and setters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    public boolean isDoor() {
        return door;
    }

    public void setDoor(boolean door) {
        this.door = door;
    }

    public String getUpdate_at() {
        return update_at;
    }

    public void setUpdate_at(String update_at) {
        this.update_at = update_at;
    }
}
