package com.example.smartlockjava.manager;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;

import java.util.Map;

public class JWTDecoder {


    public static Map<String, Claim> decodeJwt(String token) {
        JWT jwt = new JWT(token);
        Map<String, Claim> claims = jwt.getClaims();
        return claims;
    }
}