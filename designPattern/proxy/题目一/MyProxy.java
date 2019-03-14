package com.gupao.vip.dynamicproxy.myproxy;


import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * 用来生成源代码的工具类
 * Created by qingbowu on 2019/3/12.
 */
public class MyProxy {

    private static final String ln = "\r\n";

    public static Object newProxyInstance(MyClassLoader loader,
                                          Class<?>[] interfaces,
                                          MyInvocationHandler h)throws IllegalArgumentException {
        try {
            //1、动态生成java源代码
            String src = generateSrc(interfaces);

            //2、Java文件输出到磁盘
            String filePath = MyProxy.class.getResource("").getPath();
            File file = new File(filePath + "$Proxy0.java");
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(src);
            fileWriter.flush();
            fileWriter.close();

            //3、手动编译生成的Java文件
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            StandardJavaFileManager manager = compiler.getStandardFileManager(null, null, null);
            Iterable iterable = manager.getJavaFileObjects(file);

            JavaCompiler.CompilationTask task = compiler.getTask(null, manager, null, null, null, iterable);
            task.call();
            manager.close();

            //4、通过自定义的MyClassLoader加载手动编译成的$Proxy0.class文件
            Class<?> proxyClass = loader.findClass("$Proxy0");
            Constructor<?> constructor = proxyClass.getConstructor(MyInvocationHandler.class);
            return constructor.newInstance(h);
//            System.out.println(src);
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;

    }

    /**
     * 手动生成代理类$Proxy0.java源代码
     * @param interfaces
     * @return
     */
    private static String generateSrc(Class<?>[] interfaces) {
        //用代码写代码
        StringBuilder sb = new StringBuilder();
        sb.append("package com.gupao.vip.dynamicproxy.myproxy;" + ln);
        sb.append("import com.gupao.vip.dynamicproxy.jdkproxy.BuyHouse;"+ln);
        sb.append("import java.lang.reflect.*;"+ln);
        sb.append("public class $Proxy0 implements "+ interfaces[0].getName() + "{" + ln);

            sb.append("MyInvocationHandler h;" + ln);
            sb.append("public $Proxy0(MyInvocationHandler h) {" + ln);
                sb.append(" this.h = h;" + ln);
            sb.append("}" + ln);

            for (Method m : interfaces[0].getMethods()){
                sb.append("public " + m.getReturnType() +" " + m.getName() +"() {" + ln);
                    sb.append("try{" + ln);
                        sb.append("Method m = " + interfaces[0].getName() +".class.getMethod(\""+m.getName()+"\",new Class[]{});" + ln);
                        sb.append("this.h.invoke(this,m,null);" + ln);
                    sb.append("}catch(Throwable e){" + ln);
                        sb.append("e.printStackTrace();" + ln);
                    sb.append("}" + ln);
                sb.append("}" + ln);
            }

        sb.append("}");

        return sb.toString();
    }

}
