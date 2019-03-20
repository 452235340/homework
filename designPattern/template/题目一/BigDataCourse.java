package com.gupao.vip.pattern.template.notextends;

/**
 * Created by qingbowu on 2019/3/20.
 */
public class BigDataCourse implements INetwork {

    private boolean needHomework = false;

    public BigDataCourse(boolean needHomework) {
        this.needHomework = needHomework;
    }

    @Override
    public void checkHomework() {
        System.out.println("检查大数据课程课后作业");
    }

    @Override
    public boolean needHomework() {
        return this.needHomework;
    }
}
