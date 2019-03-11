package com.gupao.vip.pattern.singlerton.hungry;

/**
 * 第一种写法
 * 恶汉模式
 * Created by qingbowu on 2019/3/10.
 */
public class HungrySingleton {

    public static final HungrySingleton hungry = new HungrySingleton();

    private HungrySingleton(){

    }

    public static HungrySingleton getInstance(){
        return hungry;
    }
}
