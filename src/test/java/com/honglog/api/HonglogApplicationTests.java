package com.honglog.api;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

class HonglogApplicationTests {

    @Test
    void jwtTest() {


        byte[] decoded = Base64.getDecoder().decode("eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0");

        String s = new String(decoded);

        System.out.println("decoded = " + s);



    }

    @Test
    void test01() {

        SecretKey secretKey1= Keys.secretKeyFor(SignatureAlgorithm.HS256);
        SecretKey secretKey2 = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        String secretString = "12345678901234567890123456789012";


        byte[] secretByte = secretString.getBytes(StandardCharsets.UTF_8);

        SecretKey secretKey3 = Keys.hmacShaKeyFor(secretByte);
        SecretKey secretKey4 = Keys.hmacShaKeyFor(secretByte);

        System.out.println("secretKey1 = " + Base64.getEncoder().encodeToString(secretKey1.getEncoded()));
        System.out.println("secretKey2 = " + Base64.getEncoder().encodeToString(secretKey2.getEncoded()));
        System.out.println("secretKey3 = " + Base64.getEncoder().encodeToString(secretKey3.getEncoded()));
        System.out.println("secretKey4 = " + Base64.getEncoder().encodeToString(secretKey4.getEncoded()));

    }

}



