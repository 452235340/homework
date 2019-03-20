package com.gupao.vip.pattern.adapter.loginadapter;

/**
 * Created by qingbowu on 2019/3/19.
 */
public class ResultMsg {

    private int code;

    private String msg;

    private Object object;

    public ResultMsg(int code, String msg, Object object) {
        this.code = code;
        this.msg = msg;
        this.object = object;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
