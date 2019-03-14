package com.gupao.vip.dynamicproxy.myproxy;

import java.lang.reflect.Method;

/**
 * 自定义动态代理类
 * Created by qingbowu on 2019/3/12.
 */
public interface MyInvocationHandler {

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable;
}
