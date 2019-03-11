package com.design.factory.simplefactory;

import com.design.factory.ICourse;

/**
 * Created by qingbowu on 2019/3/11.
 */
public class CourseFactory {

    public ICourse create(Class<? extends ICourse> clazz){
        if(null != clazz){
            try {
                return clazz.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
