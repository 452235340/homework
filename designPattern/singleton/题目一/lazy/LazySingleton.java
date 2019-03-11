package com.gupao.vip.pattern.singlerton.lazy;

/**
 * 懒汉模式
 * Created by qingbowu on 2019/3/10.
 */
public class LazySingleton {

    private static LazySingleton singleton = null;

    private LazySingleton(){};

    /**
     * 在方法上加synchronized这种写法可能会导致类死锁而不可用，所以可以使用双重校验锁
     * @return
     */
    public synchronized static  LazySingleton getInstance(){
        if (null == singleton){
            singleton = new LazySingleton();
        }
        return singleton;
    }
}
