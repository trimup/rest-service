package com.lihe.controller;

import com.lihe.common.BaseController;
import com.lihe.common.Constant;
import com.lihe.common.Msg;
import com.lihe.event.register.*;
import com.lihe.service.RegisterService;
import com.lihe.service.UserService;
import com.lihe.service.VerifyCodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by trimup on 2016/7/21.
 * 注册接口服务
 */
@RestController
@RequestMapping(value = "/lihe/userAccount/register")
public class RegisterController extends BaseController {

    private static final Logger L = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    private RegisterService registerService;
    @Autowired
    private UserService userService;


    //注册新用户
    @RequestMapping(value = "/registerNewUser", method = {RequestMethod.POST})
    public Msg registerNewUser(@RequestBody RegisterUserEvent event) throws Exception {
        L.info("register-NewUser request is "+ event.toString());
        Msg msg = registerService.insertNewUser(event);
        L.info("register-NewUser=>" + msg);
        return msg;
    }

    //开通基金账户 添加第一张银行卡
    @RequestMapping(value = "/registerFundAccount", method = {RequestMethod.POST})
    public Msg registerFundAccount(@RequestBody AddBankCardEvent event) throws Exception {
        L.info("registerFundAccount request param is "+event.toString());
        if(event.checkReFundAccount())
            return new Msg(Constant.FAIL,"参数有误");
       return registerService.fundAccountRegister(event);
    }

    //新增基金账户 添加 >=2 张银行卡
    @RequestMapping(value = "/addFundAccount", method = {RequestMethod.POST})
    public Msg addFundAccount(@RequestBody AddBankCardEvent event) throws Exception {
        L.info(" addFundAccount request param is "+event.toString());
        if(event.checkaddTradeAccount())
            return new Msg(Constant.FAIL,"参数有误");
        return registerService.addTradeAccount(event);
    }

    //开通基金账户短信签约 发送短信
    @RequestMapping(value = "/registerFundAccountSendSms", method = {RequestMethod.POST})
    public Msg registerFundAccountSendSms(@RequestBody AddBankCardEvent event) throws Exception {
        L.info(" registerFundAccountSendSms request param is "+event.toString());
        if(event.checkReVerifyCode())
            return new Msg(Constant.FAIL,"参数有误");
        return registerService.registerFundAccountSendSms(event);
    }


    //创建加密分享链接
    @RequestMapping(value = "/createShareLink", method = {RequestMethod.POST})
    public Msg createShareLink(@RequestBody CreateShareLinkEvent event) throws Exception {
        if(event.orEmpty())
            return  new Msg(1,"参数有误");
        Msg msg =new Msg();
        ShareLinkReturnEvent data = registerService.createShareLink(event);
        msg.setData(data);
        msg.setCode(Msg.SUCCESS);
        msg.setMsg(Msg.SUCCESS_MSG);
        return  msg;
    }

    //分享注册
    @RequestMapping(value = "/reNewUserByShare", method = {RequestMethod.POST})
    public Msg reNewUserByShare(@RequestBody ShareReUserEvent event) throws Exception {
        L.info(" share register-NewUser request is "+ event.toString());
        Msg msg = registerService.reNewUserByShare(event);
        L.info("share register-NewUser=>" + msg);
        return msg;
    }


    //添加紧急联系人
    @RequestMapping(value = "/addEmerContact", method = {RequestMethod.POST})
    public Msg addEmerContact(@RequestBody AddEmeryContactEvent event) throws Exception {
        if(event.isCheck())
            return  new Msg(Constant.FAIL,"参数有误");
        Msg msg =userService.checkToken(event.getUser_tid(),event.getToken());
        if(msg.getCode() !=Constant.SUCCESS)
            return  msg;
        registerService.addEmerContact(event);
        msg.setCode(Constant.SUCCESS);
        msg.setMsg(Constant.SUCCESS_MSG);
        return msg;
    }

    //查询推荐用户
    @RequestMapping(value = "/queryReUser", method = {RequestMethod.POST})
    public Msg queryReUser(@RequestBody QueryReUserEvent event) throws Exception {
        if(event.isCheck())
            return  new Msg(Constant.FAIL,"参数有误");
        Msg msg =userService.checkToken(event.getUser_tid(),event.getToken());

        RecommendUserData data =registerService.queryReUser(event.getUser_tid());
        msg.setCode(Constant.SUCCESS);
        msg.setMsg(Constant.SUCCESS_MSG);
        msg.setData(data);
        return msg;
    }


}
