package com.zzh.youchatbackend.common.entity.vo;

import lombok.Data;

@Data
public class ResponseVO<T> {
    private int code;
    private String msg;
    private T data;
    public Long timestamp;

    public ResponseVO() {
        this.timestamp = System.currentTimeMillis();
    }

    public static <T> ResponseVO<T> success(T data) {
        ResponseVO<T> response = new ResponseVO<>();
        response.setCode(200);
        response.setMsg("success");
        response.setData(data);
        return response;
    }

    public static <T> ResponseVO<T> fail(int code, String msg) {
        ResponseVO<T> response = new ResponseVO<>();
        response.setCode(code);
        response.setMsg(msg);
        return response;
    }
}
