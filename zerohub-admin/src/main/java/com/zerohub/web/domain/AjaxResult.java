package com.zerohub.web.domain;

import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Objects;

/**
 * 操作消息提醒
 *
 * @author zhangyu
 * @date 2022/9/29 15:36
 */
public class AjaxResult extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private static final String CODE_TAG = "code";

    /**
     * 返回内容
     */
    private static final String MSG_TAG = "msg";

    /**
     * 数据对象
     */
    private static final String DATA_TAG = "data";

    /**
     * 初始化一个新创建的 AjaxResult 对象，使其表示一个空消息。
     */
    public AjaxResult() {
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象，使其表示一个空消息
     *
     * @param code 状态码
     * @param msg  返回内容
     */
    public AjaxResult(int code, String msg) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     *
     * @param code 状态码
     * @param msg  返回内容
     * @param data 数据对象
     */
    public AjaxResult(int code, String msg, Object data) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        // if (Objects.nonNull(data)) {
        //     super.put(DATA_TAG, data);
        // }
        super.put(DATA_TAG, data);
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static AjaxResult success() {
        return success("操作成功");
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static AjaxResult success(String msg) {
        return success(msg, null);
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static AjaxResult success(Object data) {
        return success("操作成功", data);
    }

    /**
     * 返回成功消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static AjaxResult success(String msg, Object data) {
        return new AjaxResult(HttpStatus.OK.value(), msg, data);
    }


    /**
     * 返回失败消息
     *
     * @return 失败消息
     */
    public static AjaxResult error() {
        return success("操作失败");
    }

    /**
     * 返回失败消息
     *
     * @return 失败消息
     */
    public static AjaxResult error(String msg) {
        return error(msg, null);
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static AjaxResult error(Object data) {
        return error("操作失败", data);
    }

    /**
     * 返回失败消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 失败消息
     */
    public static AjaxResult error(String msg, Object data) {
        return new AjaxResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg, data);
    }
}
