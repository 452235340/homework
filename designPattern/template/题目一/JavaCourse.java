package com.gupao.vip.pattern.template.notextends;

import com.gupao.vip.pattern.template.course.NetworkCourse;

/**
 * Created by qingbowu on 2019/3/18.
 */
public class JavaCourse implements INetwork {

    @Override
    public void checkHomework() {
        System.out.println("检查Java架构师课后作业");
    }

    @Override
    public boolean needHomework() {
        return false;
    }


}
