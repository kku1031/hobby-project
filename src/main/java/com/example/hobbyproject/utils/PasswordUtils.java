package com.example.hobbyproject.utils;

import lombok.experimental.UtilityClass;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@UtilityClass
public class PasswordUtils {

    private static final int RESET_PASSWORD_LENGTH = 10;
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 입력한 패스워드를 해시된 패스워드랑 비교하는 함수
    public static boolean equalPassword(String password, String encryptedPassword) {
        return passwordEncoder.matches(password, encryptedPassword);
    }

    // 패스워드를 암호화하여 리턴하는 함수
    public static String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    // 렌덤 난수 메소드
    public static String getResetPassword() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, RESET_PASSWORD_LENGTH);
    }
}
