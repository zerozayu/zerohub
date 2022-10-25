package com.zerohub.web.domain;


import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Data
public class AjaxResult<T> {
    private static final long serialVersionUID = 1L;

    public static final String MSG_SUCCESS = "操作成功";
    public static final String MSG_ERROR = "操作失败";
    public static final Integer CODE_SUCCESS = 200;

    /**
     * Http状态响应码
     */
    private Integer code;

    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 响应信息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 私有化无参构造
     */
    private AjaxResult() {
    }

    public AjaxResult(Integer code, String errorCode, String message, T data) {
        this.code = code;
        this.errorCode = errorCode;
        this.message = message;
        this.data = data;
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static <T> AjaxResult<T> success() {
        return success(MSG_SUCCESS);
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static <T> AjaxResult<T> success(String msg) {
        return success(msg, null);
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static <T> AjaxResult<T> success(T data) {
        return success(MSG_SUCCESS, data);
    }

    /**
     * 返回成功消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static <T> AjaxResult<T> success(String msg, T data) {
        return success(HttpStatus.OK.value(), ErrorCodeEnum.OK.getCode(), msg, data);
    }

    /**
     * 返回成功消息
     *
     * @param errorCode 错误码
     * @param msg       返回内容
     * @param data      数据对象
     * @return 成功消息
     */
    public static <T> AjaxResult<T> success(String errorCode, String msg, T data) {
        return success(HttpStatus.OK.value(), errorCode, msg, data);
    }

    /**
     * 返回成功消息
     *
     * @param code      http状态响应码
     * @param errorCode 错误码
     * @param msg       返回内容
     * @param data      数据对象
     * @return 成功消息
     */
    public static <T> AjaxResult<T> success(Integer code, String errorCode, String msg, T data) {
        return new AjaxResult<>(code, errorCode, msg, data);
    }


    /**
     * 返回失败消息
     *
     * @return 失败消息
     */
    public static <T> AjaxResult<T> error() {
        return error(MSG_ERROR);
    }

    /**
     * 返回失败消息
     *
     * @return 失败消息
     */
    public static <T> AjaxResult<T> error(String msg) {
        return error(null, msg, null);
    }

    /**
     * 返回失败消息
     *
     * @return 失败消息
     */
    public static <T> AjaxResult<T> error(String errorCode, String msg) {
        return error(errorCode, msg, null);
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static <T> AjaxResult<T> error(String errorCode, T data) {
        return error(errorCode, MSG_ERROR, data);
    }

    /**
     * 返回失败消息
     *
     * @param errorCode 错误码
     * @param msg       返回内容
     * @param data      数据对象
     * @return 失败消息
     */
    public static <T> AjaxResult<T> error(String errorCode, String msg, T data) {
        return new AjaxResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorCode, msg, data);
    }

    /**
     * 返回失败消息
     *
     * @param code      响应码
     * @param errorCode 错误码
     * @param msg       返回内容
     * @param data      数据对象
     * @return 失败消息
     */
    public static <T> AjaxResult<T> error(Integer code, String errorCode, String msg, T data) {
        return new AjaxResult<>(code, errorCode, msg, data);
    }
}
