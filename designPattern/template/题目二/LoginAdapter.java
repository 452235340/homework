package com.gupao.vip.pattern.adapter.loginadapter.v2.adapters;

import com.gupao.vip.pattern.adapter.loginadapter.ResultMsg;

/**
 * 此接口是非必要的，不加也行
 * Created by qingbowu on 2019/3/19.
 */
public interface LoginAdapter {

    boolean isSupport(Object adapter);

    ResultMsg login(String key,Object adapter);
}
