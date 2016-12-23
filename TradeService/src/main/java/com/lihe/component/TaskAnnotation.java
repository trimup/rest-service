/**
 * @Version 1.0.0
 * Copyright (c) 2016上海相诚金融-版权所有
 */
package com.lihe.component;

import com.lihe.service.HSOrderPurchaseService;
import com.lihe.service.HSOrderRedeemService;
import com.lihe.service.UserInvestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Class TaskAnnotation
 * @Description
 * @Author 张超超
 * @Date 2016/9/8 17:42
 */
@Component
@EnableScheduling
public class TaskAnnotation {

    Logger L = LoggerFactory.getLogger(TaskAnnotation.class);

    @Autowired
    private HSOrderPurchaseService hsOrderPurchaseService;

    @Autowired
    private HSOrderRedeemService hsOrderRedeemService;

    @Autowired
    private UserInvestService userInvestService;

//    /**
//     * 定时任务：查询交易状态并修改
//     * 每天凌晨0点05分执行
//     */
    @Scheduled(cron="0 0 11,14 * * ?")
    public void updateTradeStatusTask() {
        L.info("@Scheduled updateTradeStatusTask begin...");
        hsOrderPurchaseService.batchHandlerTradeResultForPurchase();
        hsOrderRedeemService.batchHandlerTradeResultForRedeem();
        L.info("@Scheduled updateTradeStatusTask end...");
    }


    /**
     * 统计用户的收益
     */
    @Scheduled(cron="0 30 0 * * ?")
    public void statUserIncomeEveryDay(){
        L.info("@Scheduled down user_fund_share_info from hs begin...");
        userInvestService.statUserIncomeEveryDay();
        L.info("@Scheduled down user_fund_share_info from hs  end...");
    }

}
