package com.pptv.cusp.coop.service.base;

import com.pptv.cusp.coop.exception.VipHandlerException;
import com.pptv.cusp.coop.util.Response;
import com.pptv.cusp.coop.vo.CoopOutOrderParamForm;

import java.util.Map;

/**
 * Created by qingbowu on 2018/12/13.
 */
public interface IFOutOrderNotifyService {

    Response outOrderNotify(Map<String,String> paramMap) throws VipHandlerException;
}
