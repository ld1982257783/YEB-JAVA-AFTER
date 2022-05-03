package com.xxxx.server;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class YebSpringBootText01 {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void test1(){

        boolean matches = passwordEncoder.matches("123",
                "$2a$10$bcA591HoLhaZLegvEgN.Pe9ho4TQFfdgwk8Bs3L8ifsmyUOMTXmWO");
        String encode = passwordEncoder.encode("123");

        System.out.println(matches);
        System.out.println(encode);
    }
}
