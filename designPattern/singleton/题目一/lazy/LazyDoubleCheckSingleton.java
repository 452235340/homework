package com.gupao.vip.pattern.singlerton.lazy;

/**
 * Created by qingbowu on 2019/3/10.
 */
public class LazyDoubleCheckSingleton {

    //为了解决cup指令重排序的问题，可以加上volatile关键字保证线程的可见性就可以解决了
    private volatile static LazyDoubleCheckSingleton singleton = null;

    private LazyDoubleCheckSingleton(){};

    /**
     * 双重校验锁
     * @return
     */
    public  static  LazyDoubleCheckSingleton getInstance(){
        if (null == singleton){
            synchronized (LazyDoubleCheckSingleton.class){
                //此处如果不再次判断，仍然有可能对象被重复new
                if (null == singleton){
                    singleton = new LazyDoubleCheckSingleton();
                    //CPU执行的时候会转化为jvm指令执行
                    //所有会发生指令重排序的问题
                    //1、分配内存给对象
                    //2、初始化对象
                    //3、将初始化好的对象和内存地址建立关联关系，也就是赋值
                    //4、用户初次访问
                }
            }
        }
        return singleton;
    }
}
