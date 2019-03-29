package com.gupao.vip.mvcframework.servlet.v2;

import com.gupao.vip.mvcframework.annotation.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;


/**
 * Created by qingbowu on 2019/3/25.
 */
public class MyDispatchServlet extends HttpServlet {

    private static final String LOCATION = "contextConfigLocation";

    private Properties properties = new Properties();

    //保存扫描的所有的类名
    private  List<String> className = new ArrayList<String>();

    //IOC容器，保存所有需要放入ioc容器中类的实例
    private Map<String,Object> IOC = new HashMap<String,Object>();

    //保存url和Method的对应关系
//    private Map<String,Method> handlerMapping = new HashMap<String,Method>();

    //思考：为什么不用Map
    //用Map的话，key只能为url
    //HandlerMapping 本身的功能就具有url和method的对应关系，已经具有Map的功能
    //根据设计原则：单一职责，最少知道原则，帮助我们更好的理解
    //从性能上来讲，与其交给Map来遍历，还不如自己直接遍历
    private List<HandlerMapping> handlerMapping = new ArrayList<HandlerMapping>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception{

        HandlerMapping handlerMapping =  getHandlerMapping(req);
        if (null == handlerMapping){
            resp.getWriter().write("404 Not Found!!!");
            return;
        }
        //获取方法的形参列表
        Class<?>[] parameterTypes = handlerMapping.getParamTypes();

        //获取请求参数
        Map<String,String[]> parameterMap = req.getParameterMap();

        Object[] paramValues = new Object[parameterTypes.length];
        for (Map.Entry<String, String[]> param : parameterMap.entrySet()) {
            String value = Arrays.toString(param.getValue())
                    .replaceAll("\\[|\\]","")
                    .replaceAll("\\s","");
            if (!handlerMapping.paramIndexMapping.containsKey(param.getKey())){continue;}
            int index = handlerMapping.paramIndexMapping.get(param.getKey());
            paramValues[index] = convert(parameterTypes[index],value);
        }

        if (handlerMapping.paramIndexMapping.containsKey(HttpServletRequest.class.getName())){
            int reqIndex = handlerMapping.paramIndexMapping.get(HttpServletRequest.class.getName());
            paramValues[reqIndex] = req;
        }
        if (handlerMapping.paramIndexMapping.containsKey(HttpServletResponse.class.getName())){
            int respIndex = handlerMapping.paramIndexMapping.get(HttpServletResponse.class.getName());
            paramValues[respIndex] = resp;
        }

        Object resultValue = handlerMapping.method.invoke(handlerMapping.controller, paramValues);
        if (null == resultValue || resultValue instanceof Void){ return;}
        resp.getWriter().write(resultValue.toString());
    }

    private HandlerMapping getHandlerMapping(HttpServletRequest req) {
        if (handlerMapping.isEmpty()){ return null;}
        //获取请求的url,绝对路径
        String url = req.getRequestURI();
        //处理成相对路径
        String contextPath = req.getContextPath();
        url = url.replaceAll(contextPath,"").replaceAll("/+","/");

        for (HandlerMapping mapping  : handlerMapping){
            if (url.equals(mapping.getUrl())){
                return mapping;
            }
        }
        return null;
    }

    //url传过来的参数都是String类型的，http是基于字符串协议
    //这里把String转为任意类型就好
    private Object convert(Class<?> type,String value){
        if (Integer.class == type){
            return Integer.valueOf(value);
        }
        //如果还有其他类型，在这里继续加if(后面优化可以用策略模式)
        return value;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            this.doDispatch(req,resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("500 Exception,Detail:" + Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {

        //1、加载配置文件
        this.loadConfig(config.getInitParameter(LOCATION));

        //2、扫描所有相关的类
        scannerPackage(properties.getProperty("scanPackage"));

        //3、初始化所有相关类的实例，即初始化IOC容器
        doInstance();

        //4、将所有IOC容器中所有有依赖关系的类，实现依赖注入，即赋值
        doAutoWirted();

        //5、构造HandlerMapping
        initHandlerMapping();

        System.out.println("my spring framework is started");

        //6、等待请求、很久url拿到Handlermapping中对应的的方法，利用反射进行调用
    }

    /**
     * 根据web.xml配置参数获取配置文件名，然后加载该配置文件
     * @param location
     */
    private void loadConfig(String location) {
        InputStream fis = null;
        try {
            fis = this.getClass().getClassLoader().getResourceAsStream(location);
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (null != fis){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 首字母小写
     * 利用ASCII码中大写字符与小写字符值相差32的规则实现首字母小写
     * 如果类名首字母是小写的会有问题
     * @param simpleName
     * @return
     */
    private String lowerFirstCase(String simpleName) {
        char[] chars = simpleName.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

    /**
     * 扫描配置文件配置的路劲下所有的类
     * @param packageName
     */
    private void scannerPackage(String packageName) {
        //将所有的包路劲转换为文件路劲
        URL url = this.getClass().getClassLoader().getResource("/" + packageName.replaceAll("\\.","/"));
        File dir = new File(url.getFile());
        for(File file : dir.listFiles()){
            if (file.isDirectory()){
                //如果是文件则继续递归
                scannerPackage(packageName + "." + file.getName());
            }else {
                if (!file.getName().endsWith(".class")){continue;}
                String classname = (packageName + "." + file.getName().replace(".class","")).trim();
                className.add(classname);
            }
        }
    }

    /**
     * 实例活所有相关类,为后面的DI操作做准备
     */
    private void doInstance() {
        if (className.isEmpty()){ return; }

        try {
            for (String className : className){
                Class<?> clazz = Class.forName(className);
                //对加了注解的类进行初始化
                if (clazz.isAnnotationPresent(MyController.class)){
                    //默认类名首字母小写
                    String beanName = lowerFirstCase(clazz.getSimpleName());
                    IOC.put(beanName,clazz.newInstance());
                }else if (clazz.isAnnotationPresent(MyService.class)){
                    //1、首先拿自定义类名
                    MyService myService = clazz.getAnnotation(MyService.class);
                    String beanName = myService.value();
                    //2、如果为空则使用类名首字母小写
                    if ("".equals(beanName.trim())){
                        beanName = lowerFirstCase(clazz.getSimpleName());
                    }
                    Object instance = clazz.newInstance();
                    IOC.put(beanName,instance);
                    //3、若未设置则按接口类型创建一个实例
                    for (Class<?> cls : clazz.getInterfaces()){
                        if (IOC.keySet().contains(cls.getName())){
                            throw new Exception("The '" +cls.getName() + "' is exists!!!");
                        }
                        IOC.put(cls.getName(),instance);
                    }
                }else {
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 依赖注入（赋值）
     */
    private void doAutoWirted() {
        if (IOC.isEmpty()){ return;}
        for (Map.Entry<String,Object> entry : IOC.entrySet()){
            //拿到每个类所有的字段(包括private修饰的)
            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            for (Field field : fields){
                if (!field.isAnnotationPresent(MyAutowired.class)){continue;}
                MyAutowired myAutowired = field.getAnnotation(MyAutowired.class);
                   String beanName = myAutowired.value().trim();
                if ("".equals(beanName)){
                    //为空，则默认使用接口类型注入
                    beanName = field.getType().getName();
                }
                field.setAccessible(true);
                try {
                    //利用反射动态给字段赋值
                    field.set(entry.getValue(),IOC.get(beanName));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 初始化HandlerMapping，将MyController中的method与url一对一的对应关系注册到HandlerMapping中
     */
    private void initHandlerMapping() {
        if (IOC.isEmpty()){ return; }
        for (Map.Entry<String,Object> entry : IOC.entrySet()){
            Class<?> clazz = entry.getValue().getClass();
            if (!clazz.isAnnotationPresent(MyController.class)){continue;}

            //保存写在类上的@MyRequestmapping("/demo")
            String baseUrl = "";
            if (clazz.isAnnotationPresent(MyRequestmapping.class)){
                MyRequestmapping myRequestmapping = clazz.getAnnotation(MyRequestmapping.class);
                baseUrl = myRequestmapping.value();
            }
            //获取所有的public方法 
            Method[] methods = clazz.getMethods();
            for (Method method : methods){
                if (!method.isAnnotationPresent(MyRequestmapping.class)){continue;}
                MyRequestmapping myRequestmapping = method.getAnnotation(MyRequestmapping.class);
                String url = ("/" + baseUrl + "/" +myRequestmapping.value()).replaceAll("/+","/");
                this.handlerMapping.add(new HandlerMapping(url,entry.getValue(),method));
                System.out.println("Mapped : " + url + "," + method.getName() );
            }
        }
    }

    class HandlerMapping{
        private String url;
        private Method method;
        private Object controller;
        //形参列表，参数的名字作为key，参数的顺序，位置作为值
        private Map<String,Integer> paramIndexMapping;

        private Class<?>[] paramTypes;

        public HandlerMapping(String url, Object controller, Method method) {
            this.url = url;
            this.method = method;
            this.controller = controller;

            this.paramTypes = method.getParameterTypes();
            paramIndexMapping = new HashMap<String,Integer>();
            putParamIndexMapping(method);
        }

        public String getUrl() {
            return url;
        }

        public Method getMethod() {
            return method;
        }

        public Object getController() {
            return controller;
        }

        public Class<?>[] getParamTypes() {
            return paramTypes;
        }

        private void putParamIndexMapping(Method method) {
            //根据方法拿到参数的注解，一个参数可能有多个注解，一个方法有多个参数，所以得到的是一个二维数组
            Annotation[][] annotations = method.getParameterAnnotations();
            for (int i=0;i<annotations.length;i++){
                for (Annotation a : annotations[i]){
                    //解析的只是MyRequestParameter
                    if (a instanceof MyRequestParameter){
                        //拿到参数名称，去http://localhost:8080/demo/query?name=zhangsan匹配
                        String paramName = ((MyRequestParameter) a).value();
                        //从req中拿到的参数列表中去找对应的key
                        if (!"".equals(paramName.trim())){
                            paramIndexMapping.put(paramName,i);
                        }
                    }
                }
            }

            //提取方法中的HttpServletRequest和HttpServletResponse参数
            Class<?>[] parameterTypes = method.getParameterTypes();
            for (int i=0;i<parameterTypes.length;i++){
                Class<?> parameterType = parameterTypes[i];
                if (parameterType == HttpServletRequest.class ||
                        parameterType == HttpServletResponse.class){
                    paramIndexMapping.put(parameterType.getName(),i);
                }

            }
        }
    }

}
