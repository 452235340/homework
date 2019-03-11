package com.design.factory.abstractfactory;

/**
 * Created by qingbowu on 2019/3/11.
 */
public class PythonNote implements INote {

    @Override
    public void edit() {
        System.out.println("编写Python笔记");
    }
}
