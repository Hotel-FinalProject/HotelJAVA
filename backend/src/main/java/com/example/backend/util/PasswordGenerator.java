package com.example.backend.util;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PasswordGenerator {

    // 비밀번호에 포함될 문자들 정의
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "@$!%*?&#";
    private static final String ALL_CHARACTERS = UPPERCASE + LOWERCASE + DIGITS + SPECIAL_CHARACTERS;

    private static final SecureRandom random = new SecureRandom();

    public static String generateRandomPassword(int length) {
        if (length < 4 || length > 12) {
            throw new IllegalArgumentException("비밀번호 길이는 4~12자여야 합니다.");
        }

        // 비밀번호 조합을 위한 리스트 생성
        List<Character> password = new ArrayList<>();

        // 각 조건을 만족하는 문자 최소 1개씩 추가
        password.add(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));
        password.add(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));
        password.add(DIGITS.charAt(random.nextInt(DIGITS.length())));
        password.add(SPECIAL_CHARACTERS.charAt(random.nextInt(SPECIAL_CHARACTERS.length())));

        // 나머지 자리는 랜덤한 문자로 채우기
        for (int i = 4; i < length; i++) {
            password.add(ALL_CHARACTERS.charAt(random.nextInt(ALL_CHARACTERS.length())));
        }

        // 생성된 리스트를 랜덤하게 섞기
        Collections.shuffle(password);

        // 리스트를 문자열로 변환
        StringBuilder shuffledPassword = new StringBuilder();
        for (char c : password) {
            shuffledPassword.append(c);
        }

        return shuffledPassword.toString();
    }
}
