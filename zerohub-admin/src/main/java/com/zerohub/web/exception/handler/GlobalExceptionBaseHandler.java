package com.zerohub.web.exception.handler;

import com.zerohub.web.domain.AjaxResult;
import com.zerohub.web.domain.ErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 *
 * @author zhangyu26
 * @date 2022/10/18
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionBaseHandler {
    // String servicePrefix = "U";
    // String moduleCode = "001";
    // String code = "0001";
    // String errorCode = servicePrefix + moduleCode + code;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public AjaxResult handleException(Exception e) {

        String msg = e.getMessage();
        if (StringUtils.isEmpty(msg)) {
            msg = "服务端错误";
        }
        log.error(msg, e);
        return AjaxResult.error(msg);
    }
}
