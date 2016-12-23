package com.lihe.controller;

import com.lihe.common.Constant;
import com.lihe.common.Msg;
import com.lihe.entity.FianPlanInfo;
import com.lihe.event.FinaPlan.ConcernedFinaPlanEvent;
import com.lihe.event.FinaPlan.FinalPlanData;
import com.lihe.event.FinaPlan.QueryFinalPlanEvent;
import com.lihe.pojo.FPDetailPojo;
import com.lihe.service.FinancialPlannerService;
import com.lihe.service.UserService;
import org.bouncycastle.cert.ocsp.Req;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by trimup on 2016/9/27.
 */
@RestController
@RequestMapping(value = "/lihe/userAccount/finaPlan")
public class FinancialPlannerController {
    private static final Logger L   =  LoggerFactory.getLogger(FinancialPlannerController.class);


    @Autowired
    private FinancialPlannerService financialPlannerService;
    @Autowired
    private UserService userService;


    /**
     * 获取所有的理财师信息
     * @param event
     * @return
     */
    @RequestMapping(value = "/queryAllFinaPlan",method = RequestMethod.POST)
    public Msg getAllFinacialPlanner(@RequestBody QueryFinalPlanEvent event){
        Msg msg =new Msg();
        FinalPlanData data =financialPlannerService.getAllFinaPlan(event);
        msg.setData(data);
        msg.setCode(Constant.SUCCESS);
        msg.setMsg(Constant.SUCCESS_MSG);
        return  msg;
    }


    /**
     * 获取所有的理财师信息
     * @param event
     * @return
     */
    @RequestMapping(value = "/getFinaPlanDetail",method = RequestMethod.POST)
    public Msg getFinaPlanDetail(@RequestBody QueryFinalPlanEvent event){
        if(event.orCheck())
            return  new Msg(Constant.FAIL,"参数有误");
        Msg msg =new Msg();
        FPDetailPojo detail =financialPlannerService.getFinaPlanDetail(event.getFp_id());
        msg.setData(detail);
        msg.setCode(Constant.SUCCESS);
        msg.setMsg(Constant.SUCCESS_MSG);
        return  msg;
    }

    /**
     * 关注理财师
     * @param event
     * @return
     */
    @RequestMapping(value = "/concernedFinaPlan",method = RequestMethod.POST)
    public Msg concernedFinaPlan(@RequestBody ConcernedFinaPlanEvent event){
        if(event.orCheck())
            return  new Msg(Constant.FAIL,"参数有误");
        Msg msg =userService.checkToken(event.getUser_tid(),event.getToken());
        if(msg.getCode() != Constant.SUCCESS)
            return  msg;
        msg =financialPlannerService.concernedFinaPlan(event);
        return  msg;
    }


}
