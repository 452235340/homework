package com.gupao.vip.dynamicproxy.myproxy;

import com.gupao.vip.dynamicproxy.jdkproxy.BuyHouse;
import com.gupao.vip.dynamicproxy.jdkproxy.Customer;
import com.gupao.vip.dynamicproxy.jdkproxy.Intermediary;
import sun.misc.ProxyGenerator;

import java.io.FileOutputStream;

/**
 * Created by qingbowu on 2019/3/11.
 */
public class BuyHouseMyProxyTest {

    public static void main(String[] args) {
        try {
            Customer customer = new Customer();
            MyIntermediary intermediary = new MyIntermediary();

            BuyHouse customerProxy = (BuyHouse)intermediary.getInstance(customer);
            System.out.println(customerProxy.getClass());
            customerProxy.buyHouse();


//            byte[] bytes = ProxyGenerator.generateProxyClass("$Proxy0", new Class[]{BuyHouse.class});
//            FileOutputStream fos = new FileOutputStream("$Proxy0.class");
//            fos.write(bytes);
//            fos.flush();
//            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
