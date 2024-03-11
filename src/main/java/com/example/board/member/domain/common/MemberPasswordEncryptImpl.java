package com.example.board.member.domain.common;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class MemberPasswordEncryptImpl implements MemberPasswordEncrypt {
    private static final int SALT_LENGTH = 16; // 솔트 길이
    private static final String HASH_ALGORITHM = "SHA-256";

    // 패스워드 해싱
    @Override
    public String encrypt(String password) {
        String salt = generateSalt();
        String saltedPassword = password + salt;
        String hashedPassword = hashString(saltedPassword);
        return salt + hashedPassword;
    }

    // 패스워드 일치 여부 확인
    public boolean match(String encryptedPassword, String plainPassword) {
        String salt = encryptedPassword.substring(0, SALT_LENGTH);
        String hashedEnteredPassword = hashString(plainPassword + salt);
        String encryptedEnteredPassword = salt + hashedEnteredPassword;
        return encryptedEnteredPassword.equals(encryptedPassword);
    }

    // 문자열 해싱
    private String hashString(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] hashedBytes = digest.digest(input.getBytes());
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to hash the string.", e);
        }
    }

    // 솔트 생성
    private String generateSalt() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        secureRandom.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt)
                .substring(0, SALT_LENGTH);
    }
}
