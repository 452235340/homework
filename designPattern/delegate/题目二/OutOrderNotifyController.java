package com.pptv.cusp.coop.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.pptv.cusp.coop.constant.Constant;
import com.pptv.cusp.coop.constant.Error;
import com.pptv.cusp.coop.dto.OrderInfoQueryDTO;
import com.pptv.cusp.coop.entity.OrderInfoEntity;
import com.pptv.cusp.coop.entity.TaskOutOrderEntity;
import com.pptv.cusp.coop.enums.ChannelEnums;
import com.pptv.cusp.coop.enums.OrderStatusEnums;
import com.pptv.cusp.coop.exception.VipHandlerException;
import com.pptv.cusp.coop.service.*;
import com.pptv.cusp.coop.util.*;
import com.pptv.cusp.coop.vo.CommonResultVO;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * 咪咕回调接口
 * Created by qingbowu on 2018/7/6.
 */
@Controller
public class OutOrderNotifyController {

    private static final Logger log = LoggerFactory.getLogger(OutOrderNotifyController.class);

    private static final int MAX_THREAD_COUNT = 50;
    @Autowired
    private MIGUService miguService;
    @Autowired
    private YiGouService yigouservice;
    @Autowired
    private ParameterAdapterService parameteradapterservice;
    @Autowired
    private OutOrderNotifyService outOrderNotifyService;
    @Autowired
    private SystemVariableService systemVariableService;
    @Autowired
    OutOrderService outOrderService;

   

    /**
     * 外部订单推送接口
     * @param request
     * @param paramMap 请求参数
     * @return
     */
    @RequestMapping(value = "/notify_order")
    @ResponseBody
    public String notifyOrder( HttpServletRequest request, @RequestBody Map<String,String> paramMap){
        String ip = HTTPClientUtils.getIpAddr(request);
        log.info("call notifyOrder ,paramMap-->[{}],ip-->[{}]", JsonUtil.toJsonStr(paramMap),ip);
        CommonResultVO result = new CommonResultVO();
        if (StringUtils.isBlank(paramMap.get("phone")) || StringUtils.isBlank(paramMap.get("channel")) || StringUtils.isBlank(paramMap.get("outOrderId")) || StringUtils.isBlank(paramMap.get("sign")) || StringUtils.isBlank(paramMap.get("productId"))
                || StringUtils.isBlank(paramMap.get("appId")) || StringUtils.isBlank(paramMap.get("orderTime"))||StringUtils.isBlank(paramMap.get("url"))){
            log.info("param empty,paramForm-->[{}]", JsonUtil.toJsonStr(paramMap));
            result.setError(Error.PARAMISNULL.getErrorCode(), Error.PARAMISNULL.getMessage());
            return ResultUtil.commonRender(result, "", "");
        }
        if (!isIpInWhiteList(ip)) {
            result.setError(Error.IP_ERROR);
            return ResultUtil.commonRender(result, "json", "");
        }
        String outOrderLock = Constant.OUT_RDER_LOCK_PREFIX.concat(paramMap.get("outOrderId")).concat("_").concat(paramMap.get("appId"));
        try {
            //统一请求正在处理中，提示稍后再试
            if (!DistributedLock.tryLock(outOrderLock)) {
                result.setError(Error.OUT_ORDER_PROCESSING);
                return ResultUtil.commonRender(result, "", "");
            }
            return outOrderNotifyService.orderNotify(paramMap);
        } catch (VipHandlerException  e) {
            log.error("call notifyOrder,error-->[{}],requestParams-->[{}]",ExceptionUtil.getTrace(e),JsonUtil.toJsonStr(paramMap));
            return ResultUtil.commonRender(new CommonResultVO(Error.createError(e.getCode(),e.getMessage())), "", "");
        }catch (Exception e) {
            log.error("call notifyOrder,error-->[{}],requestParams-->[{}]",ExceptionUtil.getTrace(e),JsonUtil.toJsonStr(paramMap));
            return ResultUtil.commonRender(new CommonResultVO(Error.SERVICE_EXCEPTION),"json","");
        } finally {
            DistributedLock.release(outOrderLock);
        }
    }

}
