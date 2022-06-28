package com.zaqbest.base.web.utils;

public class PropUtil {
    public static String getValue(String key){

        String value = System.getProperty(key);
        if (value != null){
            return value;
        }

        return System.getenv(key);
    }
}
