package com.example.backend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class PasswordEncryptor {

    @Bean
    public CommandLineRunner encryptPassword() {
        return args -> {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String rawPassword = "@ghdrlfehd1";
            String encryptedPassword = encoder.encode(rawPassword);
            System.out.println("암호화된 비밀번호: " + encryptedPassword);
        };
    }
}


