package com.lihe.controller;

import com.lihe.common.Constant;
import com.lihe.common.Msg;
import com.lihe.event.priOrder.MyOrderData;
import com.lihe.event.priOrder.OrderPriProductEvent;
import com.lihe.event.priOrder.QueryMyOrderEvent;
import com.lihe.service.PriOrderService;
import com.lihe.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by trimup on 2016/9/7.
 */
@RestController
@RequestMapping(value = "/lihe/trade/priOrder")
public class PriOrderController {

    private static final Logger L = LoggerFactory.getLogger(PriOrderController.class);


    @Autowired
    private PriOrderService priOrderService;
    @Autowired
    private UserService userService;


    /**
     * 预约私募基金
     */
    @RequestMapping(value = "/orderPrivateFund" ,method = {RequestMethod.GET,RequestMethod.POST})
    public Msg orderPrivateFund(@RequestBody OrderPriProductEvent event){
              if(event.orCheck())
                  return  new Msg(Constant.FAIL,"参数有误");
        Msg msg =userService.checkToken(event.getUser_tid(),event.getToken());
        if(msg.getCode() !=Constant.SUCCESS)
            return  msg;
        msg =priOrderService.orderPrivateFund(event);
        return  msg;
    }

    /**
     * 我预约的私募基金
     */
    @RequestMapping(value = "/myOrderPrivateFund" ,method = {RequestMethod.GET,RequestMethod.POST})
    public Msg myOrderPrivateFund(@RequestBody QueryMyOrderEvent event){
        if(event.orCheck())
            return  new Msg(Constant.FAIL,"参数有误");
        Msg msg =userService.checkToken(event.getUser_tid(),event.getToken());
        if(msg.getCode() !=Constant.SUCCESS)
            return  msg;
        MyOrderData data  =priOrderService.myOrderPrivateFund(event);
        msg.setCode(Constant.SUCCESS);
        msg.setData(data);
        msg.setMsg(Constant.SUCCESS_MSG);
        return  msg;
    }

}
