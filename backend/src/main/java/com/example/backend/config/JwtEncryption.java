package com.example.backend.config;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.util.Base64URL;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class JwtEncryption {
    public static void main(String[] args) throws Exception {
        // 암호화 키 생성
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        SecretKey key = keyGen.generateKey();

        // JWE Header 정의
        JWEHeader header = new JWEHeader(JWEAlgorithm.DIR, EncryptionMethod.A128GCM);

        // 페이로드에 정보를 담습니다.
        Payload payload = new Payload("Hello, World!");

        // JWE 객체 생성
        JWEObject jweObject = new JWEObject(header, payload);

        // 암호화 수행
        jweObject.encrypt(new DirectEncrypter(key));

        // 암호화된 JWT 문자열
        String jwtString = jweObject.serialize();
        System.out.println("Encrypted JWT: " + jwtString);
    }
}
