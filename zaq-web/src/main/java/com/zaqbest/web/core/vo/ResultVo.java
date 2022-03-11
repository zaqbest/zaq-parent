package com.zaqbest.web.core.vo;

import com.zaqbest.web.constant.ErrCodeComm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chenbin
 * 2021/11/2 21:03
 */
@ApiModel("应答结果")
@Data
public class ResultVo<T> {

    /**
     * 返回结果状态。
     */
    @ApiModelProperty(value = "返回结果状态。",required = true)
    private String code;

    /**
     * 返回结果状态。
     */
    private Integer status;

    /**
     * 返回结果消息
     */
    @ApiModelProperty(value = "返回结果消息", required = true)
    private String msg;

    /**
     * 返回具体信息
     */
    @ApiModelProperty("业务数据")
    private T data;

    private ResultVo() {
    }

    private ResultVo(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 成功
     * @param data
     * @return
     */
    public static<T> ResultVo<T> success(T data) {
        return new ResultVo(ErrCodeComm.ERR_CODE_000000_CODE, ErrCodeComm.ERR_CODE_000000_MSG, data);
    }

    /**
     * 失败
     * @param data
     * @param msg
     * @return
     */
    public static<T> ResultVo<T> error(String errorCode,String msg, Object data ) {
        return new ResultVo(errorCode, msg, data);
    }

    /**
     * 失败
     * @param msg
     * @return
     */
    public static ResultVo error(String errorCode,String msg) {
        return new ResultVo(errorCode, msg, null);
    }

    /**
     * 失败
     * @param msg
     * @return
     */
    public static<T> ResultVo<T> error(String msg) {
        return new ResultVo<T>(ErrCodeComm.ERR_CODE_999999_CODE, msg, null);
    }

    /**
     * 成功失败验证
     *
     * @author fengzhiwen
     * @param result
     * @return
     */
    public static boolean isSuccess(ResultVo result) {
        if (ErrCodeComm.ERR_CODE_000000_CODE.equals(result.getCode())) {
            return true;
        }
        return false;
    }

}
