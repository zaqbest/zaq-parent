package com.zaqbest.base.comm.dto;

/**
 * 常用API返回对象接口
 * Created by macro on 2019/4/19.
 */
public interface IErrorCode {
    /**
     * 返回码
     */
    Integer getCode();

    /**
     * 返回信息
     */
    String getMessage();
}
