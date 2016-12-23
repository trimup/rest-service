package com.lihe.controller;

import com.lihe.common.Constant;
import com.lihe.common.Msg;
import com.lihe.event.SendSmsEvent;
import com.lihe.service.SendSMSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by trimup on 2016/7/25.
 * 发送短信接口
 */
@RestController
@RequestMapping(value = "/lihe/smsService/sendSMS")
public class SendSMSController {

    private static final Logger L   =  LoggerFactory.getLogger(SendSMSController.class);


    @Autowired
    private SendSMSService sendSMSService;

    @RequestMapping("/sendSmsByYunRong")
    public Msg sendSms(SendSmsEvent event){
        if(event.isCheck())
            return  new Msg(Constant.FAIL,"参数错误");
        return sendSMSService.sendSmsWithYunRong(event);

    }

    @RequestMapping("/sendSmsByChuangLan")
    public Msg sendSmsByChuangLan(@RequestBody SendSmsEvent event)throws Exception{
        if(event.isCheck())
            return  new Msg(Constant.FAIL,"参数错误");
        return sendSMSService.sendSmsWithChuangLan(event);

    }



}
