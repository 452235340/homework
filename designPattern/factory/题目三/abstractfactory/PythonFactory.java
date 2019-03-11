package com.design.factory.abstractfactory;

/**
 * Created by qingbowu on 2019/3/11.
 */
public class PythonFactory implements CourseFactory {

    @Override
    public INote createNote() {
        return new PythonNote();
    }

    @Override
    public IVideo createVideo() {
        return new PythonVideo();
    }
}
