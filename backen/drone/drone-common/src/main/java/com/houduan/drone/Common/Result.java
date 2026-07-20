package com.houduan.drone.Common;


import lombok.Data;


@Data
public class Result<T> {
    private Integer code; //响应状态码
    private String message; //响应消息

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }

    public Result() {
    }

    public Result(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}


