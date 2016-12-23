/**
 * @Version 1.0.0
 * Copyright (c) 2016上海相诚金融-版权所有
 */
package com.lihe.controller;

import com.lihe.common.Constant;
import com.lihe.common.Msg;
import com.lihe.entity.HSOrderPurchase;
import com.lihe.entity.HSOrderRedeem;
import com.lihe.entity.HSProductInfo;
import com.lihe.event.hsTrade.RedeemProductEvent;
import com.lihe.service.HSOrderPurchaseService;
import com.lihe.service.HSOrderRedeemService;
import com.lihe.service.HSUserFundInfoService;
import com.lihe.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * @Class HSOrderRedeemController
 * @Description
 * @Author 张超超
 * @Date 2016/9/12 17:31
 */
@RestController
@RequestMapping(value = "/lihe/trade/hsRedeem")
public class HSOrderRedeemController {

    private Logger L = LoggerFactory.getLogger(HSOrderRedeemController.class);

    @Autowired
    private HSOrderRedeemService hsOrderRedeemService;
    /**
     * 赎回基金
     * @param req
     *          参数
     * @return
     */
    @RequestMapping(value = "/redeemProduct", method = RequestMethod.POST)
    public Msg redeemProduct(@RequestBody RedeemProductEvent req) throws ParseException {
        L.info("method redeemProduct params : " + req);
        Msg msg = new Msg();
        msg = hsOrderRedeemService.redeemProduct(req);
        return msg;
    }
}
