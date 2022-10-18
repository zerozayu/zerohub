package com.zerohub.web.domain;

/**
 * 结果枚举类
 *
 * @author zhangyu26
 * @date 2022/10/18
 */
public enum ErrorCodeEnum {

    OK("00000", "一切 ok"),
    A0001("A0001", "用户端错误"),
    A0100("A0100", "用户注册错误")
    // todo (zhangyu26, 2022-10-18 17:17:34) : 错误码声明 -- 参考《Java开发手册》附录3
    ;

    /**
     * 错误码
     */
    private final String code;

    /**
     * 中文描述
     */
    private final String message;

    ErrorCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
