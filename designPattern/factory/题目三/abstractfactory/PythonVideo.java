package com.design.factory.abstractfactory;

/**
 * Created by qingbowu on 2019/3/11.
 */
public class PythonVideo implements IVideo {


    @Override
    public void record() {
        System.out.println("录制Python视频");
    }
}
