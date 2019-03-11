package com.gupao.vip.pattern.singlerton.lazy;

/**
 * 静态内部类的方式
 * 性能最优的一种写法
 * 全程没有用到synchornized
 * Created by qingbowu on 2019/3/10.
 */
public class LazyInnerClassSingleton {

    //虽然构造方法私有了，但仍旧逃不过反射的法眼
    private LazyInnerClassSingleton(){

    }

    //懒汉式单利
    //LazyHolder里面的逻辑需要等到外部方法调用时才执行
    //巧妙利用了内部类的特性
    //JVM底层执行逻辑，完美的避免了线程安全的问题
    public static final LazyInnerClassSingleton getInstance(){
        return LazyHolder.lazy;
    }

    private static class LazyHolder{
        private static final LazyInnerClassSingleton lazy = new LazyInnerClassSingleton();
    }
}
