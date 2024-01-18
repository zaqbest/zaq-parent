package com.zaqbest.core.utils;

import com.zaqbest.core.exception.BusinessException;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Exceptions {

    /**
     * 将CheckedException转换为UncheckedException.
     */
    public static RuntimeException unchecked(Throwable ex) {
        if (ex instanceof RuntimeException) {
            return (RuntimeException) ex;
        } else {
            return new RuntimeException(ex);
        }
    }

    /**
     * 将ErrorStack转化为String.
     */
    public static String getStackTraceAsString(Throwable ex) {
        StringWriter stringWriter = new StringWriter();
        ex.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    /**
     * 获取组合本异常信息与底层异常信息的异常描述, 适用于本异常为统一包装异常类，底层异常才是根本原因的情况。
     */
    public static String getErrorMessageWithNestedException(Throwable ex) {
        Throwable nestedException = ex.getCause();
        if (nestedException == null) {
            return ex.getMessage();
        } else {
            return new StringBuilder().append(ex.getMessage()).append(" nested exception is ")
                    .append(nestedException.getClass().getName()).append(":").append(nestedException.getMessage())
                    .toString();
        }
    }

    /**
     * 获取异常的Root Cause.
     */
    public static Throwable getRootCause(Throwable ex) {
        Throwable cause;
        while ((cause = ex.getCause()) != null) {
            ex = cause;
        }
        return ex;
    }

    /**
     * 判断异常是否由某些底层的异常引起.
     */
    public static boolean isCausedBy(Exception ex,
                                     @SuppressWarnings("unchecked") Class<? extends Exception>... causeExceptionClasses) {
        Throwable cause = ex;
        while (cause != null) {
            for (Class<? extends Exception> causeClass : causeExceptionClasses) {
                if (causeClass.isInstance(cause)) {
                    return true;
                }
            }
            cause = cause.getCause();
        }
        return false;
    }

    /**
     * ====================================================================
     * 函 数 名： @param msg
     * 功 能： 抛出异常
     * ----------------------------------------------------------------------
     * 修改记录 ：
     * 日 期  版本 修改人 修改内容
     * 2016年10月19日 v0.0.1 liang.lei 创建
     * ====================================================================
     */
    public static void throwBusinessException(String msg) {
        throw new BusinessException(msg);
    }

    /**
     * ====================================================================
     * 功 能： 抛出带有错误码和错误描述的异常
     * ----------------------------------------------------------------------
     * 修改记录 ：
     * 日 期  版本 修改人 修改内容
     * 2019年4月3日 v0.0.1 fan 创建
     * ====================================================================
     */
    public static void throwBusinessException(Integer errorCode, String errorMsg) {
        throw new BusinessException(errorCode, errorMsg);
    }
}
