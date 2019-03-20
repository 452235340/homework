package com.gupao.vip.pattern.adapter.loginadapter.v2;

import com.gupao.vip.pattern.adapter.loginadapter.ResultMsg;
import com.gupao.vip.pattern.adapter.loginadapter.v1.service.SignService;
import com.gupao.vip.pattern.adapter.loginadapter.v2.adapters.*;

/**
 * Created by qingbowu on 2019/3/19.
 */
public class PassportForThirdAdapter extends SignService implements IPassportForThird{



    @Override
    public ResultMsg loginForQQ(String openId) {
        return this.processLogin(openId, LoginForQQAdapter.class);
    }

    @Override
    public ResultMsg loginFoWeChat(String openId) {
        return this.processLogin(openId, LoginForWeChatAdapter.class);
    }

    @Override
    public ResultMsg loginForToken(String token) {
        return this.processLogin(token, LoginForTokenAdapter.class);
    }

    @Override
    public ResultMsg loginForTelphone(String phone, String coed) {
        return this.processLogin(phone, LoginForTelAdapter.class);
    }

    @Override
    public ResultMsg loginForRegister(String username, String password) {
        super.register(username,password);
        return super.login(username,password);
    }

    //这里用到了简单工厂模式及策略模式
    private ResultMsg processLogin(String key, Class<? extends LoginAdapter> claszz){
        try {
            LoginAdapter loginAdapter = claszz.newInstance();
            if (loginAdapter.isSupport(loginAdapter)){
                return loginAdapter.login(key,loginAdapter);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
