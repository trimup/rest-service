/**
 * @Version 1.0.0
 * Copyright (c) 2016上海相诚金融-版权所有
 */
package com.lihe.controller;

import com.lihe.common.Msg;
import com.lihe.event.hsOrder.QueryHSOrderListEvent;
import com.lihe.event.hsUserFund.QueryHsUserFundForRedeemEvent;
import com.lihe.service.HSUserFundInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Class HsUserFundInfoController
 * @Description
 * @Author 张超超
 * @Date 2016/9/14 9:48
 */
@RestController
@RequestMapping(value = "/lihe/trade/hsUserFund")
public class HSUserFundInfoController {

    private Logger L = LoggerFactory.getLogger(HSUserFundInfoController.class);

    @Autowired
    private HSUserFundInfoService hsUserFundInfoService;

    /**
     * 用户基金数据查询（赎回页面用）
     * @param req
     *          参数
     * @return
     */
    @RequestMapping(value = "/queryHsUserFundForRedeem", method = RequestMethod.POST)
    public Msg queryHsUserFundForRedeem(@RequestBody QueryHsUserFundForRedeemEvent req) {
        Msg msg = new Msg();
        msg = hsUserFundInfoService.queryHsUserFundForRedeem(req);
        return msg;
    }

    /**
     * 用户基金数据查询（赎回页面用）
     * @param req
     *          参数
     * @return
     */
    @RequestMapping(value = "/queryHsUserFundForRedeem", method = RequestMethod.GET)
    public Msg queryHsUserFundForRedeemGet(QueryHsUserFundForRedeemEvent req) {
        Msg msg = new Msg();
        msg = hsUserFundInfoService.queryHsUserFundForRedeem(req);
        return msg;
    }

//    /**
//     * 基金交易查询
//     * @param req
//     *          参数
//     * @return
//     */
//    @RequestMapping(value = "/queryHSOrderList", method = RequestMethod.POST)
//    public Msg queryHSOrderList(@RequestBody QueryHSOrderListEvent req) {
//        Msg msg = hsUserFundInfoService.queryHSOrderList(req);
//        return msg;
//    }
}
