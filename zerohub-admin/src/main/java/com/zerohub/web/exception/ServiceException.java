package com.zerohub.web.exception;

import lombok.Data;
import lombok.NonNull;

/**
 * 自定义业务异常
 *
 * @author zhangyu26
 * @date 2022/10/18
 */
public class ServiceException extends RuntimeException {
    private static final Long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误提示
     */
    private String message;

    /**
     * 错误明细，内部调试错误
     */
    private String detailMessage;

    /**
     * 空构造方法，避免反序列化问题
     * todo ???
     */
    public ServiceException() {

    }

    public ServiceException(String message) {
        this.message = message;
    }

    public ServiceException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
    }
}
