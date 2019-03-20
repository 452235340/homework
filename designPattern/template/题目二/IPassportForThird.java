package com.gupao.vip.pattern.adapter.loginadapter.v2;

import com.gupao.vip.pattern.adapter.loginadapter.ResultMsg;

/**
 * 只扩展
 * Created by qingbowu on 2019/3/19.
 */
public interface IPassportForThird {

    /**
     * QQ登陆
     * @param openId
     * @return
     */
    ResultMsg loginForQQ(String openId);

    /**
     * 微信登陆
     * @param openId
     * @return
     */
    ResultMsg loginFoWeChat(String openId);



    /**
     * token登陆
     * @param token
     * @return
     */
    ResultMsg loginForToken(String token);

    /**
     * 手机号登陆
     * @param phone
     * @param coed
     * @return
     */
    ResultMsg loginForTelphone(String phone,String coed);


    /**
     * 登陆并注册
     * @param username
     * @param password
     * @return
     */
    ResultMsg loginForRegister(String username,String password);
}
