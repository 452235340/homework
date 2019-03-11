package com.design.factory.abstractfactory;

/**
 * Created by qingbowu on 2019/3/11.
 */
public class JavaCourseFactory  implements CourseFactory{


    @Override
    public INote createNote() {
        return new JavaNote();
    }

    @Override
    public IVideo createVideo() {
        return new JavaVideo();
    }
}
