package com.my.utils;

import java.util.UUID;

public class IdUtils {
    public static String getUUID(){
        return UUID.randomUUID().toString().replaceAll("[^0-9]", "").substring(0,4);
    }
}
