package com.zerohub.web.exception.handler;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import com.zerohub.web.domain.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

/**
 * 全局异常处理-数据库
 *
 * @author zhangyu26
 * @date 2022/10/20
 */
@RestControllerAdvice
@Slf4j
@ConditionalOnClass({SQLException.class, ElasticsearchException.class, DuplicateKeyException.class})
public class GlobalExceptionDbHandler {

    /**
     * 主键冲突
     * @param e
     * @return
     */
    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public AjaxResult handleException(DuplicateKeyException e) {
        log.error("主键冲突", e);
        // todo (zhangyu26, 2022-10-20 09:49:29) : 主键冲突时errorCode定义
        return AjaxResult.error(HttpStatus.CONFLICT.value(), null, "主键冲突", null);
    }
}
