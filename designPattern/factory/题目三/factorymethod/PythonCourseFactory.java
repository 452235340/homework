package com.design.factory.factorymethod;

import com.design.factory.ICourse;
import com.design.factory.PythonCourse;

/**
 * Created by qingbowu on 2019/3/11.
 */
public class PythonCourseFactory implements ICourseFactory {


    @Override
    public ICourse create() {
        return new PythonCourse();
    }
}
