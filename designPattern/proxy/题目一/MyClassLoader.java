package com.gupao.vip.dynamicproxy.myproxy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * Created by qingbowu on 2019/3/12.
 */
public class MyClassLoader extends ClassLoader {

    private File classPathFile;

    public MyClassLoader(){
        String classPath = MyClassLoader.class.getResource("").getPath();
        this.classPathFile = new File(classPath);
    }

    /**
     * 自定义的MyClassLoader需要继承ClassLoader并重写findClass方法
     * @param name class文件名
     * @return
     * @throws ClassNotFoundException
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String className = MyClassLoader.class.getPackage().getName() + "." + name;
        if (null != classPathFile){
            File classFile = new File(classPathFile,name.replaceAll("\\.","/")+".class");
            if (classFile.exists()){
                FileInputStream fis = null;
                ByteArrayOutputStream bos = null;
                try {
                    fis = new FileInputStream(classFile);
                    bos = new ByteArrayOutputStream();
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = fis.read(buf)) != -1){
                        bos.write(buf,0,len);
                    }
                    //调用JVM的defineClass方法来加载手动生成的class文件的二进制字节，然后生成一个class对象返回
                    return defineClass(className,bos.toByteArray(),0,bos.size());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }
}
