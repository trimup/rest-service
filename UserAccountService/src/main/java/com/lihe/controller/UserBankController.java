package com.lihe.controller;

import com.lihe.common.Constant;
import com.lihe.common.Msg;
import com.lihe.event.userBank.QueryUserBankEvent;
import com.lihe.event.userBank.QueryUserBankForRedeemEvent;
import com.lihe.service.UserBankService;
import com.lihe.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by trimup on 2016/8/17.
 * 用户银行信息
 */
@RestController
@RequestMapping("/lihe/userAccount/userBank")
public class UserBankController {


    private static final Logger L   =  LoggerFactory.getLogger(UserBankController.class);



    @Autowired
    private UserService userService;
    @Autowired
    private UserBankService userBankService;


    /**
     * 获取用户银行卡信息
     * @param event
     * @return
     */
    @RequestMapping(value="/getUserBank",method = RequestMethod.POST)
    public Msg queryUserBank(@RequestBody QueryUserBankEvent event){
        if(event.isCheck())
            return  new Msg(Constant.FAIL,"参数有误");
        Msg msg =userService.checkToken(event.getUser_tid(),event.getToken());
        if(msg.getCode() != Constant.SUCCESS)
            return  msg;
        msg =userBankService.getUserBankInfo(event.getUser_tid());
        return msg;
    }
    /**
     * 获取银行信息
     * @param
     * @return
     */
    @RequestMapping(value="/getBankCodeInfo",method = {RequestMethod.GET,RequestMethod.POST})
    public Msg getBankCodeInfo(){
        Msg msg = new Msg();
        msg.setCode(Constant.SUCCESS);
        msg.setMsg(Constant.SUCCESS_MSG);
        msg.setData(userBankService.getBankCodeInfo());
        return msg;
    }

    /**
     * 获取用户银行卡信息
     * @param event
     * @return
     */
    @RequestMapping(value="/getUserBankInfoForRedeem",method = RequestMethod.POST)
    public Msg getUserBankInfoForRedeem(@RequestBody QueryUserBankForRedeemEvent event){
        if(event.isCheck())
            return  new Msg(Constant.FAIL,"参数有误");
        Msg msg =userService.checkToken(event.getUser_tid(),event.getToken());
        if(msg.getCode() != Constant.SUCCESS)
            return  msg;
        msg =userBankService.getUserBankInfoForRedeem(event.getUser_tid(), event.getFund_code());
        return msg;
    }

}
