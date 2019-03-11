package com.gupao.vip.pattern.singlerton.register;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 容器式单例(注册)
 * 对象方便管理，也属于懒汉加载
 *  但是存在线程安全问题，所以需要加上synchronized修饰代码块，所以也会影响性能
 * Created by qingbowu on 2019/3/10.
 */
public class ContainerSingleton {
    private static  Map<String,Object>  ioc = new ConcurrentHashMap<String, Object>();

    private ContainerSingleton(){ }

    public static Object getBean(String classname){
        //为了保证线程安全，故加上synchronized
        synchronized (ioc){
            if (!ioc.containsKey(classname)){
                Object obj = null;
                try {
                    Class<?> cls = Class.forName(classname);
                    Constructor<?> constructor = cls.getDeclaredConstructor();
                    obj = constructor.newInstance();
                    ioc.put(classname,obj);
                }catch (Exception e){
                    e.printStackTrace();
                }
                return obj;
            }
            return ioc.get(classname);
        }
    }
}
