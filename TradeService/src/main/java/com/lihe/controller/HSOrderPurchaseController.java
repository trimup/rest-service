/**
 * @Version 1.0.0
 * Copyright (c) 2016上海相诚金融-版权所有
 */
package com.lihe.controller;

import com.lihe.common.Constant;
import com.lihe.common.Msg;
import com.lihe.event.hsTrade.CalcPurchaseRateEvent;
import com.lihe.event.hsTrade.PurchaseProductEvent;
import com.lihe.event.hsTrade.QueryPurchaseResultEvent;
import com.lihe.service.HSOrderPurchaseService;
import com.lihe.service.HSUserFundInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.text.ParseException;

/**
 * @Class HsOrderController
 * @Description
 * @Author 张超超
 * @Date 2016/9/5 14:51
 */
@RestController
@RequestMapping(value = "/lihe/trade/hsPurchase")
public class HSOrderPurchaseController {

    private Logger L = LoggerFactory.getLogger(HSOrderPurchaseController.class);

    @Autowired
    private HSOrderPurchaseService hsOrderPurchaseService;

    /**
     * 购买公募基金
     * @param req
     *          参数
     * @return
     */
    @RequestMapping(value = "/purchaseProduct", method = RequestMethod.POST)
    public Msg purchaseProduct(@RequestBody PurchaseProductEvent req) throws Exception {
        L.info("method purchaseProduct params : " + req);
        Msg msg = hsOrderPurchaseService.purchaseProduct(req);
        return msg;
    }


    /**
     * 购买公募基金
     * @param
     *
     * @return
     */
    @RequestMapping(value = "/purchaseProductResult", method = RequestMethod.POST)
    public Msg purchaseProductResult(@RequestBody QueryPurchaseResultEvent event) throws ParseException {
        L.info("method purchaseProductResult params : " + event.toString());
        Msg msg = hsOrderPurchaseService.
                purchaseProductResult(event.getAllot_no(),event.getTrade_acco(),event.getOrder_id());
        return msg;
    }


    /**
     * 计算申购基金费率
     * @param
     *
     * @return
     */
    @RequestMapping(value = "/calcPurchaseRate", method = RequestMethod.POST)
    public Msg calcPurchaseRate(@RequestBody CalcPurchaseRateEvent event) throws Exception {
        L.info("method calcPurchaseRate params : " + event.toString());
        if(event.orCheck())
            return  new Msg(Constant.FAIL,"参数有误");
        Msg msg = hsOrderPurchaseService.calcPurchaseRate(event);
        return msg;
    }



}
