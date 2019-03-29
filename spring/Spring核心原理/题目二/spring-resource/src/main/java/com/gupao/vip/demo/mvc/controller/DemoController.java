package com.gupao.vip.demo.mvc.controller;

import com.gupao.vip.demo.service.IDemoService;
import com.gupao.vip.demo.service.impl.DemoService;
import com.gupao.vip.mvcframework.annotation.MyAutowired;
import com.gupao.vip.mvcframework.annotation.MyController;
import com.gupao.vip.mvcframework.annotation.MyRequestParameter;
import com.gupao.vip.mvcframework.annotation.MyRequestmapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by qingbowu on 2019/3/26.
 */
@MyController
@MyRequestmapping("/demo")
public class DemoController {

    @MyAutowired
    private IDemoService demoService;

    @MyRequestmapping("/query")
    public String queryName(HttpServletRequest request,@MyRequestParameter("name")String name){
        return demoService.queryName(name);
    }

    @MyRequestmapping("/add")
    public String add(HttpServletRequest request, HttpServletResponse response, @MyRequestParameter("a") Integer a, @MyRequestParameter("b") Integer b){
        return (a + " + " + b + " = " + ( a + b ));
    }

    @MyRequestmapping("/sub")
    public String sub(HttpServletRequest request, HttpServletResponse response, @MyRequestParameter("a") Integer a, @MyRequestParameter("b") Integer b){
        return (a + " - " + b + " = " + ( a - b ));
    }
}
