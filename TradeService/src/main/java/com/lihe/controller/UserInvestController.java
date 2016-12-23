package com.lihe.controller;

import com.lihe.AppConfig;
import com.lihe.common.Constant;
import com.lihe.common.Msg;
import com.lihe.event.hsOrder.QueryHSOrderListEvent;
import com.lihe.event.userInvest.*;
import com.lihe.pojo.UserAccountPojo;
import com.lihe.service.UserInvestService;
import com.lihe.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

/**
 * Created by trimup on 2016/9/5.
 * 用户投资controller
 */
@RestController
@RequestMapping(value = "/lihe/trade/userInvest")
public class UserInvestController {

    private static final Logger L   =  LoggerFactory.getLogger(UserInvestController.class);


    @Autowired
    private AppConfig appConfig;

    @Autowired
    private UserInvestService userInvestService;
    @Autowired
    private UserService userService;

    /**
     * 获取该用户投资的公募基金列表
     * @return
     */
    @RequestMapping(value = "/getMyInvestList" ,method = {RequestMethod.GET,RequestMethod.POST})
    public Msg getUserPubFundList(@RequestBody QueryUserInvestEvent event)throws Exception{
        if(event.orCheck())
            return  new Msg(Constant.FAIL,"参数有误");
        Msg msg =userService.checkToken(event.getUser_tid(),event.getToken());
        if(msg.getCode() !=Constant.SUCCESS)
            return  msg;

        msg=userInvestService.getUserInvestList(event);
        return  msg;
    }

    /**
     * 休息用户的 分红方式
     * @return
     */
    @RequestMapping(value = "/modifyUserFundBonus" ,method = {RequestMethod.GET,RequestMethod.POST})
    public Msg modifyUserFundBonus(@RequestBody ModifyBonusEvent event) throws Exception{
        if(event.orCheck())
            return  new Msg(Constant.FAIL,"参数有误");
        Msg msg =userService.checkToken(event.getUser_tid(),event.getToken());
        if(msg.getCode() !=Constant.SUCCESS)
            return  msg;
        msg =userInvestService.modifyUserFundBonus(event);
        return  msg;
    }


    /**
     * 获取用户账户
     * @return
     */
    @RequestMapping(value = "/getUserAccount" ,method = {RequestMethod.GET,RequestMethod.POST})
    public Msg getUserAccount(@RequestBody QueryUserAccountEvent event) throws Exception{
        if(event.orCheck())
            return  new Msg(Constant.FAIL,"参数有误");
        Msg msg =userService.checkToken(event.getUser_tid(),event.getToken());
        if(msg.getCode() !=Constant.SUCCESS)
            return  msg;
        UserAccountPojo userAccountPojo =userInvestService.getMyAccount(event.getUser_tid());
        msg.setData(userAccountPojo);
        msg.setMsg(Constant.SUCCESS_MSG);
        return  msg;
    }


    /**
     * 获取用户收益记录
     * @return
     */
    @RequestMapping(value = "/getUserIncome" ,method = {RequestMethod.GET,RequestMethod.POST})
    public Msg getUserIncome(@RequestBody QueryUserIncomeEvent event) throws Exception{
        if(event.orCheck())
            return  new Msg(Constant.FAIL,"参数有误");
        Msg msg =userService.checkToken(event.getUser_tid(),event.getToken());
        if(msg.getCode() !=Constant.SUCCESS)
            return  msg;
        UserIncomeData data =userInvestService.queryUserIncome(event);
        msg.setData(data);
        msg.setMsg(Constant.SUCCESS_MSG);
        return  msg;
    }


        /**
     * 基金交易查询
     * @param req
     *          参数
     * @return
     */
    @RequestMapping(value = "/queryHSOrderList", method = RequestMethod.POST)
    public Msg queryHSOrderList(@RequestBody QueryHSOrderListEvent req) throws ParseException {
        if(req.orCheck())
            return  new Msg(Constant.FAIL,"参数有误");
        Msg msg =userService.checkToken(req.getUserId(),req.getToken());
        if(msg.getCode() !=Constant.SUCCESS)
            return  msg;
         msg = userInvestService.queryHSOrderList(req);
        return msg;
    }

        /**
     * 基金交易查询
     * @param req
     *          参数
     * @return
     */
    @RequestMapping(value = "/queryHSRedeeOrderList", method = RequestMethod.POST)
    public Msg queryHSRedeeOrderList(@RequestBody QueryHSOrderListEvent req) throws ParseException {
        if(req.orCheck())
            return  new Msg(Constant.FAIL,"参数有误");
        Msg msg =userService.checkToken(req.getUserId(),req.getToken());
        if(msg.getCode() !=Constant.SUCCESS)
            return  msg;
         msg = userInvestService.queryHSRedeeOrderList(req);
        return msg;
    }


    @RequestMapping(value = "/testBus")
    public String testBus() throws ParseException {
        return appConfig.getTEST_BUS();
    }


}
