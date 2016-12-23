package com.lihe.controller;

import com.lihe.AppConfig;
import com.lihe.SendingBean;
import com.lihe.TimeSource;
import com.lihe.common.Msg;
import com.lihe.event.register.RequestVeriCodeEvent;
import com.lihe.service.RegisterService;
import com.lihe.service.VerifyCodeService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

/**
 * Created by trimup on 2016/7/25.
 */
@Slf4j
@RestController
@RequestMapping(value = "/lihe/userAccount/verifyCode")
public class VerifyCodeController {



    private static final Logger L = LoggerFactory.getLogger(VerifyCodeController.class);


    @Autowired
    private VerifyCodeService verifyCodeService;

    @Autowired
    private TimeSource timeSource;


    @Autowired
    private SendingBean sendingBean;


    @RequestMapping(value = "/getVerifyCode", method = {RequestMethod.POST})
    public Msg getVerifyCode(@RequestBody RequestVeriCodeEvent requestEvent) {
        log.info("get verifyCode request param is "+requestEvent.toString());
        Msg msg = verifyCodeService.getVerifyCode(requestEvent);
        return msg;
    }


//    @RequestMapping(value = "/testMessage")
//    public String testMessage() throws ParseException {
//      return timeSource.sendMessage();
//    }


    @RequestMapping(value = "/sendingBean")
    public void sendingBean() throws ParseException {
        sendingBean.sayHello("hello");
    }


}
