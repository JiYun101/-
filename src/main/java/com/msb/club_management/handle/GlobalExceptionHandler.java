package com.msb.club_management.handle;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理全局异常的控制器方法。
     * 当发生异常时，此方法将捕获异常并返回一个包含错误信息的ResponseEntity对象。
     *
     * @param e 抛出的异常对象
     * @return 返回一个包含错误信息的ResponseEntity对象，其中HTTP状态码为500
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseEntity<String> handleException(Exception e) {
        // 根据不同异常类型定制错误信息
        String message = e.getMessage();
        if (message.contains("(using password: YES)")) {
            // 处理涉及密码的异常
            if (!message.contains("'root'@'")) {
                message = "PU Request failed with status code 500";
            } else if (message.contains("'root'@'localhost'")) {
                message = "P Request failed with status code 500";
            }
        } else if(message.contains("Table") && message.contains("doesn't exist")) {
            // 处理表不存在的异常
            message = "T Request failed with status code 500";
        } else if (message.contains("Unknown database")) {
            // 处理未知数据库异常
            message = "U Request failed with status code 500";
        } else if (message.contains("edis")) {
            // 处理与edis相关的异常
            message = "R Request failed with status code 500";
        } else if (message.contains("Failed to obtain JDBC Connection")) {
            // 处理获取JDBC连接失败的异常
            message = "C Request failed with status code 500";
        } else if (message.contains("SQLSyntaxErrorException")) {
            // 处理SQL语法错误异常
            message = "S Request failed with status code 500";
        }
        // 返回自定义的错误信息和HTTP状态码
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
