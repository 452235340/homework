package com.gupao.vip.pattern.singlerton.threadLocal;

/**
 * ThreadLocal单例模式
 * 伪线程安全，只能保证在当前线程内安全
 * 以当前线程为key，实例为value存储在ThreadLocalMap(容器)中，当线程去取值的时其实就是去ThreadLocalMap中去取
 * 所以ThreadLocal单例模式也是一种注册式单例
 * Created by qingbowu on 2019/3/10.
 */
public class ThreadLocalSingleton {

    private ThreadLocalSingleton(){}

    private static final ThreadLocal<ThreadLocalSingleton> THREAD_LOCAL_instance =
            new ThreadLocal<ThreadLocalSingleton>(){
                @Override
                protected ThreadLocalSingleton initialValue() {
                    return new ThreadLocalSingleton();
                }
    };

    public static ThreadLocalSingleton getInstance(){
        return THREAD_LOCAL_instance.get();
    }
}
