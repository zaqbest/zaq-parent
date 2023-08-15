package com.zaqbest.base.web.utils;

import com.zaqbest.base.comm.enums.ResultCodeEnum;
import com.zaqbest.base.web.domain.CommonResult;

public class CommonHelper {
    public static boolean isSuccess(CommonResult result){
        return result != null && result.getCode() == ResultCodeEnum.SUCCESS.getCode();
    }
}
