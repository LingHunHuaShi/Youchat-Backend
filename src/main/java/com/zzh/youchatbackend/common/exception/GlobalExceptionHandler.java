package com.zzh.youchatbackend.common.exception;

import com.zzh.youchatbackend.common.entity.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ResponseEntity<ResponseVO<Void>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return new ResponseEntity<>(ResponseVO.fail(400, "Malformed JSON Request: " + e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseBody
    public ResponseEntity<ResponseVO<Void>> handleNoResourceFoundException(NoResourceFoundException e) {
        return new ResponseEntity<>(ResponseVO.fail(400, "Resource Not Found: " + e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RedisException.class)
    public ResponseEntity<ResponseVO<Void>> handleRedisException(RedisException e) {
        return new ResponseEntity<>(ResponseVO.fail(500, "Redis Error: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ResponseVO<Void>> handleBusinessException(BusinessException e) {
        return new ResponseEntity<>(ResponseVO.fail(500, "Business Error: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ResponseVO<Void>> handleException(Exception e) {
        log.error("Error class: {}, message: {}", e.getClass(), e.getMessage());
        return new ResponseEntity<>(ResponseVO.fail(500, "Internal server error"), HttpStatus.BAD_REQUEST);
    }

}
