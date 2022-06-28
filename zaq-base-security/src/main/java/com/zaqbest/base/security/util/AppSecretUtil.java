package com.zaqbest.base.security.util;

import com.zaqbest.base.web.exception.Asserts;

public class AppSecretUtil {
    public static void validateSecret(String appSecret, String appSecretToCheck){
        if (!appSecret.equals(appSecretToCheck)){
            Asserts.fail("appSecret验证失败，请确认是否正确");
        }
    }
}
