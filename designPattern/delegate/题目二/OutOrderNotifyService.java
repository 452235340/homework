package com.pptv.cusp.coop.service;

import com.alibaba.fastjson.JSONObject;
import com.pptv.cusp.coop.constant.Constant;
import com.pptv.cusp.coop.constant.Error;
import com.pptv.cusp.coop.dao.OrderIndexDao;
import com.pptv.cusp.coop.dao.OutOrderInfoDao;
import com.pptv.cusp.coop.dao.TaskOutOrderDAO;
import com.pptv.cusp.coop.entity.OrderIndexEntity;
import com.pptv.cusp.coop.entity.OutOrderInfoEntity;
import com.pptv.cusp.coop.entity.TaskOutOrderEntity;
import com.pptv.cusp.coop.entity.VipMobileSectionEntity;
import com.pptv.cusp.coop.enums.AgreementEnums;
import com.pptv.cusp.coop.enums.ChannelEnums;
import com.pptv.cusp.coop.enums.OrderStatusEnums;
import com.pptv.cusp.coop.exception.VipHandlerException;
import com.pptv.cusp.coop.util.*;
import com.pptv.cusp.coop.vo.CommonResultVO;
import com.pptv.cusp.coop.vo.RightsAddVO;
import com.pptv.cusp.log.ErrorLog;
import com.pptv.cusp.utils.DateHelper;
import com.pptv.cusp.utils.MobileMmDSA;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by qingbowu on 2018/12/13.
 */
@Service
public class OutOrderNotifyService {

    private static final Logger logger = LoggerFactory.getLogger(OutOrderNotifyService.class);
    private static final Logger error = LoggerFactory.getLogger(ErrorLog.class);

    @Autowired
    private SystemVariableService systemVariableService;
    @Autowired
    private CoopCustomizeServiceRouter coopCustomizeServiceRouter;
    @Autowired
    private OutOrderInfoDao outOrderInfoDao;
    @Autowired
    private CommonService commonService;
    @Autowired
    private TaskOutOrderDAO taskOutOrderDAO;
    @Autowired
    private CoopOrderRelationshipService coopOrderRelationshipService;
    @Autowired
    private CoopVipService coopVipService;
    @Autowired
    private CoopVipServicePoxy coopVipServicePoxy;
    @Autowired
    private OrderIndexDao orderIndexDao;


    /**
     * 处理外部订单推送
     * @param paramMap
     * @return
     * @throws VipHandlerException
     */
    public String orderNotify(Map<String,String> paramMap) throws VipHandlerException{
        //业务是否支持
        if (!isSupport(paramMap.get("channel"),paramMap.get("phone"))){
            throw new VipHandlerException(Error.BUSINESS_NON_SUPPORT);
        }
        //验签
        String key = systemVariableService.getSystemConfigValueByKey(paramMap.get("channel").concat("_").concat(paramMap.get("appId")));
        this.checkSign(paramMap,key);
        OutOrderInfoEntity outorderInfoEntity = outOrderInfoDao.queryOrderByChannelAndInnerOrderId(paramMap.get("phone"), paramMap.get("outOrderId"), paramMap.get("channel"));
        logger.info("query outorderInfoEntity from db , orderInfoEntity-->[{}]", JsonUtil.toJsonStr(outorderInfoEntity));
        if (null != outorderInfoEntity){
            throw new VipHandlerException(Error.createError(Error.ORDER_INFO_EXIST.getErrorCode(), Error.ORDER_INFO_EXIST.getMessage()), "json", "");
        }
        String innerOrderId = OrderIdWorker.nextId();
        paramMap.put("innerOrderId",innerOrderId);
        Response response = coopCustomizeServiceRouter.getOutOrderNotifyHandleService(paramMap.get("channel")).outOrderNotify(paramMap);
        logger.info("call orderNotify response-->[{}]", JsonUtil.toJsonStr(response));
        CommonResultVO commonResultVO = new CommonResultVO();
        if (null != response && Constant.SUCCESS.equals(response.getCode())){
            this.saveOrderInfo(paramMap);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("orderUrl",response.getData());
            commonResultVO.put("data",jsonObject);
        }else {
            commonResultVO.setError(Error.SERVICE_EXCEPTION);
        }
        return ResultUtil.commonRender(commonResultVO, "json", "");
    }

    private boolean isSupport(String channel, String phone) {
        if (ChannelEnums.MIGU.getChannel().equals(channel)){
            VipMobileSectionEntity vipMobileSectionEntity = coopVipServicePoxy.queryPhoneNumberLocation(phone);
            if (null == vipMobileSectionEntity || vipMobileSectionEntity.getDesc().contains(ChannelEnums.YNMOBILE.getDescription())||vipMobileSectionEntity.getDesc().contains(ChannelEnums.ANHMOBILE.getDescription())||vipMobileSectionEntity.getDesc().contains(ChannelEnums.ZJMOBILE.getDescription())||vipMobileSectionEntity.getDesc().contains(ChannelEnums.HEBEIMOBILE.getDescription())||vipMobileSectionEntity.getDesc().contains(ChannelEnums.SCMOBILE.getDescription())||vipMobileSectionEntity.getDesc().contains("湖北移动")){
                return false;
            }
        }
        return true;
    }


   //省略部分代码



    
}
