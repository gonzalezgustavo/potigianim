package com.example.potigianim.utils;

import java.util.Random;

public class StringUtils {
    public static String generateRandomString(int length) {
        char[] chars1 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        StringBuilder sb1 = new StringBuilder();
        Random random1 = new Random();

        for (int i = 0; i < length; i++)
        {
            char c1 = chars1[random1.nextInt(chars1.length)];
            sb1.append(c1);
        }

        return sb1.toString();
    }
}
