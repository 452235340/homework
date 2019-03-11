package com.design.factory;

/**
 * Created by qingbowu on 2019/3/11.
 */
public class PythonCourse implements ICourse {


    @Override
    public void record() {
        System.out.println("录制python课程");
    }
}
