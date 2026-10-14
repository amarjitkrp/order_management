package com.example.demo.securityConfig;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class PasswordTest {

    private final PasswordEncoder passwordEncoder;

    public PasswordTest(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void testPassword() {
        String password = "Hello@124";
        String hash = passwordEncoder.encode(password);

        System.out.println("Encrypted password: " + hash);
        boolean matched = passwordEncoder.matches(password, hash);
 
        System.out.println("Password matched: " + matched);
    }
    
}