/*
 * 版权信息: © 聚均科技
 */
package com.zaqbest.base.web.utils;

import cn.hutool.core.util.StrUtil;
import jdk.nashorn.internal.runtime.logging.Logger;
import lombok.extern.slf4j.Slf4j;

/**
 * ***************************************************************************
 *创建时间 : 2020年6月11日
 *实现功能 : 敏感信息处理
 *作者 : fan
 *版本 : v1.0.0
-----------------------------------------------------------------------------
 *修改记录:
 *日 期 版本 修改人 修改内容
 *2020年6月11日 v1.0.0 fan 创建
 ****************************************************************************
 */
@Slf4j
public class SensitiveInfoUtils {
	
	/**
	 * ====================================================================
	 *功 能： 身份证号显示最后四位，其他隐藏。共计18位或者15位。<例子：*************5762>
	----------------------------------------------------------------------
	 *修改记录 ：
	 *日 期  版本 修改人 修改内容
	 *2020年6月11日 v1.0.0 fan 创建
	====================================================================
	 */
    public static String maskIdNo(String idNo) {
    	if (StrUtil.isEmpty(idNo)) {
    		return "";
    	}
    	if(idNo.length() == 15) {
    		return StrUtil.replace(idNo, 0, 11, '*');
    	}
        return StrUtil.replace(idNo, 0, 14, '*');
    }
    
    /**
     * ====================================================================
     *功 能： 手机号码前三位，后四位，其他隐藏<例子:138*****1234>
    ----------------------------------------------------------------------
     *修改记录 ：
     *日 期  版本 修改人 修改内容
     *2020年6月11日 v1.0.0 fan 创建
    ====================================================================
     */
    public static String maskMobile(String mobile) {
        if (StrUtil.isEmpty(mobile)) {
            return "";
        }
        return StrUtil.replace(mobile, 3, 7, '*');
    }
    
    /**
     * ====================================================================
     *功 能： 固定号码，后四位，其他隐藏<例子：****1234>
    ----------------------------------------------------------------------
     *修改记录 ：
     *日 期  版本 修改人 修改内容
     *2020年6月11日 v1.0.0 fan 创建
    ====================================================================
     */
    public static String maskTelPhone(String phoneNo) {
        if (StrUtil.isEmpty(phoneNo)) {
            return phoneNo;
        }
        return StrUtil.replace(phoneNo, 0, phoneNo.length() - 4, '*');
    }
    
    /**
     * ====================================================================
     *功 能： 邮箱前缀仅显示第一个字母，前缀其他隐藏，用星号代替，@及后面的地址显示<例子:g**@163.com>
    ----------------------------------------------------------------------
     *修改记录 ：
     *日 期  版本 修改人 修改内容
     *2020年6月11日 v1.0.0 fan 创建
    ====================================================================
     */
    public static String maskEmail(String email) {
        if (StrUtil.isEmpty(email)) {
            return "";
        }
        int index = StrUtil.indexOf(email, '@');
        if(index <= 1) {
        	return email;
        }
        return StrUtil.replace(email, 1, index, '*');
    }


    public static void main(String[] args) {
        String id18 = "321321199001195837";
        String phone = "18501637434";
        String telPhone = "021-11119999";
        String mail = "lipan@qq.com";

        log.info("{} => {}", id18, maskIdNo(id18));
        log.info("{} => {}", phone, maskMobile(phone));
        log.info("{} => {}", telPhone, maskTelPhone(telPhone));
        log.info("{} => {}", mail, maskEmail(mail));
    }
}
