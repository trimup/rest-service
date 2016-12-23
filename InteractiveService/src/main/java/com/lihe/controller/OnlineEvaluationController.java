package com.lihe.controller;

import com.lihe.Event.EvalResultEvent;
import com.lihe.Event.QueryOnlineEvalQuesEvent;
import com.lihe.common.Constant;
import com.lihe.common.Msg;
import com.lihe.hundsunreply.RequestHSQuestionReturn;
import com.lihe.service.OnLineEvaluationService;
import com.lihe.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;

/**
 * Created by trimup on 2016/7/27.
 * 在线评测系统
 */
@RestController
@RequestMapping(value = "/lihe/interactive/onlineEvaluation")
public class OnlineEvaluationController {

    private static final Logger L =LoggerFactory.getLogger(OnlineEvaluationController.class);

    private static final SimpleDateFormat Format = new SimpleDateFormat("yyyyMMddHHmmss");


    @Autowired
    private UserService userService;
    @Autowired
    private OnLineEvaluationService onLineEvaluationService;

    /**
     * 获取恒生在线评测 题目
     * @return
     */
    @RequestMapping(value = "getOnlineEvalQues",method= RequestMethod.POST)
    public Msg getHundSunPaperinfo(@RequestBody QueryOnlineEvalQuesEvent event){
        if(event.orCheck())
            return  new Msg(Constant.FAIL,"参数有误");
        Msg msg =userService.checkToken(event.getUser_tid(),event.getToken());
        if(msg.getCode() !=Constant.SUCCESS)
            return  msg;
        return  onLineEvaluationService.getHundSunPaperinfo(event.getUser_tid());
    }


    /**
     * 获取在线评测结果
     */
    @RequestMapping(value = "getOnlineEvalResult",method= RequestMethod.POST)
    public Msg getOnlineEvalResult(@RequestBody EvalResultEvent event){
        if(event.isCheck())
            return new Msg(Constant.FAIL,"参数错误");
        Msg  msg= userService.checkToken(event.getUser_tid(),event.getToken());
        if(msg.getCode() !=Constant.SUCCESS)
            return msg;
        msg =onLineEvaluationService.getOnlineEvalResult(event);
        return msg;
    }





}
