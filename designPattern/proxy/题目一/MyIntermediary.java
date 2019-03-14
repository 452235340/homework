package com.gupao.vip.dynamicproxy.myproxy;

import com.gupao.vip.dynamicproxy.jdkproxy.BuyHouse;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by qingbowu on 2019/3/12.
 */
public class MyIntermediary implements MyInvocationHandler {

    private BuyHouse buyHousePerson;

    public Object getInstance(BuyHouse buyHousePerson) throws Exception {
        this.buyHousePerson = buyHousePerson;
        Class<?> cls = buyHousePerson.getClass();
        return MyProxy.newProxyInstance(new MyClassLoader(),cls.getInterfaces(),this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        before();
        Object invoke = method.invoke(buyHousePerson, args);
        after();
        return invoke;
    }

    private void before(){
        System.out.println("需要买房吗？我手上有大量房源");
    }

    private void after(){
        System.out.println("没问题，不过你得先交茶水费哟！");
    }
}
