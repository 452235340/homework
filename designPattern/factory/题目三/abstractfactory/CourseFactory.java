package com.design.factory.abstractfactory;

/**
 * Created by qingbowu on 2019/3/11.
 */
public interface CourseFactory {

    INote createNote();

    IVideo createVideo();
}
