package com.example.Transfero.util;

import java.security.SecureRandom;
import java.util.Random;

public class ShortCodeGenerator {


    public static String generateCode() {
        String chars = System.getenv("GEN_CODE_CHARS"); // Get from environment
        if (chars == null || chars.isEmpty()) {
            throw new RuntimeException("GEN_CODE_CHARS environment variable not set");
        }

        StringBuilder sb = new StringBuilder();
        Random rand = new SecureRandom();
        for (int i = 0; i < 6; i++) {
            sb.append(chars.charAt(rand.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
