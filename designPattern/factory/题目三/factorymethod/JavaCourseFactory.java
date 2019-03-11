package com.design.factory.factorymethod;

import com.design.factory.ICourse;
import com.design.factory.JavaCourse;

/**
 * Created by qingbowu on 2019/3/11.
 */
public class JavaCourseFactory implements ICourseFactory {

    @Override
    public ICourse create() {
        return new JavaCourse();
    }
}
