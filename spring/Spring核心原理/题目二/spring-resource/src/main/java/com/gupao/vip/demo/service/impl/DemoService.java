package com.gupao.vip.demo.service.impl;

import com.gupao.vip.demo.service.IDemoService;
import com.gupao.vip.mvcframework.annotation.MyService;

/**
 * Created by qingbowu on 2019/3/26.
 */
@MyService
public class DemoService implements IDemoService {
    @Override
    public String queryName(String name) {
        return "my name is " + name;
    }


}
