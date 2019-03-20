package com.gupao.vip.pattern.adapter.loginadapter.v1.service;

import com.gupao.vip.pattern.adapter.loginadapter.Member;
import com.gupao.vip.pattern.adapter.loginadapter.ResultMsg;

/**
 * Created by qingbowu on 2019/3/19.
 */
public class SignService {

    /**
     * 注册
     * @param username
     * @param password
     * @return
     */
    public ResultMsg register(String username,String password){
        return new ResultMsg(0,"注册成功",new Member(username,password));
    }

    /**
     * 登陆
     * @param username
     * @param password
     * @return
     */
    public ResultMsg login(String username,String password){
        return new ResultMsg(0,"登陆成功",new Member(username,password));
    }
}
