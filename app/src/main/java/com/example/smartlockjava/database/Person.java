package com.example.smartlockjava.database;

import java.util.List;

public class Person {
    private String userName;
    private String password;
    private float[] faceVector;

    public Person(String userName, String password, float[] faceVector) {
        this.userName = userName;
        this.password = password;
        this.faceVector = faceVector;
    }

    public Person(String userName, float[] faceVector) {
        this.userName = userName;
        this.faceVector = faceVector;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public float[] getFaceVector() {
        return faceVector;
    }

    public void setFaceVector(float[] faceVector) {
        this.faceVector = faceVector;
    }

    public String getUsername() {
        return userName;
    }

    public static Person from(com.example.smartlockjava.dto.PersonSync person) {
        return new Person(person.getUsername(), convertStringToFloatArray(person.getFaceVector()));

    }

    public static float[] convertStringToFloatArray(String str) {
        // Remove the brackets from the string
        str = str.replace("[", "").replace("]", "");

        // Split the string by comma
        String[] numbersStr = str.split(",");

        // Parse each element to a float
        float[] numbers = new float[numbersStr.length];
        for (int i = 0; i < numbersStr.length; i++) {
            numbers[i] = Float.parseFloat(numbersStr[i].trim()); // Use trim() to remove any leading or trailing spaces
        }

        return numbers;
    }
}
