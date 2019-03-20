package com.pptv.cusp.coop.service;

import com.pptv.cusp.coop.enums.ChannelEnums;
import com.pptv.cusp.coop.enums.SystemInnerEnums;
import com.pptv.cusp.coop.exception.VipHandlerException;
import com.pptv.cusp.coop.service.base.*;
import com.pptv.cusp.coop.util.BaseError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Calendar;

@Service
public class CoopCustomizeServiceRouter {

    
    @Autowired
    private MIGUService miguService;
    @Autowired 
	private CustomizeMobileMMService customizeMobileMMService;


    /**
     *根据渠道获取该渠道处理外部推送订单service
     * @param channel
     * @return
     */
    public IFOutOrderNotifyService getOutOrderNotifyHandleService(String channel) {
        if (ChannelEnums.MIGU.getChannel().equals(channel)) {
            return miguService;
        }else if (ChannelEnums.MOBILE_MM.getChannel().equals(channel)){
            return customizeMobileMMService;
        }
        return null;
    }


}
