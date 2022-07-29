package com.zaqbest.base.rpc.dubbo.dto;

import cn.hutool.core.util.StrUtil;
import com.zaqbest.base.comm.enums.ResultCodeEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class RpcResult<T> implements Serializable {

    /**
     * 是否响应成功
     */
    private Boolean success;
    /**
     * 响应状态码
     */
    private Integer code;
    /**
     * 响应数据
     */
    private T data;
    /**
     * 错误信息
     */
    private String message;

    // 构造器开始
    /**
     * 无参构造器(构造器私有，外部不可以直接创建)
     */
    private RpcResult() {
        this.code = ResultCodeEnum.SUCCESS.getCode();
        this.success = true;
        this.message = ResultCodeEnum.SUCCESS.getMessage();
    }
    /**
     * 有参构造器
     * @param obj
     */
    private RpcResult(T obj) {
        this.code = ResultCodeEnum.SUCCESS.getCode();
        this.data = obj;
        this.success = true;
        this.message = ResultCodeEnum.SUCCESS.getMessage();
    }

    private RpcResult(ResultCodeEnum resultCode) {
        this.success = false;
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    private RpcResult(ResultCodeEnum resultCode, String msg) {
        this.success = false;
        this.code = resultCode.getCode();
        this.message = StrUtil.isEmpty(msg) ? resultCode.getMessage() : msg;
    }

    private RpcResult(Integer code, String msg) {
        this.success = false;
        this.code = code;
        this.message = msg;
    }

    /**
     * 通用返回成功（没有返回结果）
     * @param <T>
     * @return
     */
    public static<T> RpcResult<T> success(){
        return new RpcResult();
    }

    /**
     * 返回成功（有返回结果）
     * @param data
     * @param <T>
     * @return
     */
    public static<T> RpcResult<T> success(T data){
        return new RpcResult<T>(data);
    }

    /**
     * 通用返回失败
     * @param resultCode
     * @param <T>
     * @return
     */
    public static<T> RpcResult<T> failure(ResultCodeEnum resultCode){
        return  new RpcResult<T>(resultCode);
    }

    public static<T> RpcResult<T> failure(String message){
        return  new RpcResult<T>(ResultCodeEnum.INTERNAL_ERROR, message);
    }

    public static<T> RpcResult<T> failure(Integer code, String message){
        return  new RpcResult<T>(code, message);
    }

    /**
     * 通用返回失败
     * @param resultCode
     * @param <T>
     * @return
     */
    public static<T> RpcResult<T> failure(ResultCodeEnum resultCode, String msg){
        return  new RpcResult<T>(resultCode, msg);
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "RpcResult{" +
                "success=" + success +
                ", code=" + code +
                ", data=" + data +
                ", message='" + message + '\'' +
                '}';
    }
}