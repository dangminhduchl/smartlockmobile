package com.example.smartlockjava;

import static com.example.smartlockjava.manager.JWTDecoder.decodeJwt;

import com.example.smartlockjava.manager.JWTDecoder;

import org.junit.Test;

public class JWTDecoderTest {
    private JWTDecoder jwtDecoder;

    @Test
    public void shoudDecodeJWT() {
        decodeJwt("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIiwiZXhwIjoxNzEyNDY3MjE0LCJpYXQiOjE3MTI0NjAwMTQsImp0aSI6ImFiYzFmN2ExYzdhZTQxYmY4M2UzODEyNGFhMzEyNmVjIiwidXNlcl9pZCI6MSwibmFtZSI6ImR1YyIsImlkIjoxfQ.gO6MeCEJpSzjEDfte7LnMB-DwjpjilNKH8GxOV7XOW0");
    }
}
