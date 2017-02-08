package com.yefeng.support.http;

/**
 * Created by yefeng on 20/01/2017.
 * http response
 */

public class HttpRes<T> {
    private int code;
    private String msg;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    private T data;

    @Override
    public String toString() {
        return "HttpRes{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
