package com.gupao.vip.pattern.singlerton.register;

/**
 * 通过枚举来实现单例模式(注册式单例)
 * 因为在序列化中JDK层面是通过全限定类名以及枚举名字来拿到枚举的实例，枚举只会加载一次且实例存在于JVM中(注册式)。
 * 所以通过序列化无法破坏枚举实现的单例
 * 而在通过反射也不能破坏枚举式单例，因为在JDK层面以及做了限制，强行这么做会抛异常
 * Created by qingbowu on 2019/3/10.
 */
public enum EnumSingleton {

    INSTANCE;

    private Object obj;

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public static EnumSingleton getInstance(){
        return INSTANCE;
    }
}
