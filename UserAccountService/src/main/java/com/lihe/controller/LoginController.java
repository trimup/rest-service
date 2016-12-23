package com.lihe.controller;

import com.google.common.base.Strings;
import com.lihe.common.BaseController;
import com.lihe.common.Constant;
import com.lihe.common.Msg;
import com.lihe.event.login.*;
import com.lihe.service.LoginService;
import com.lihe.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by trimup on 2016/7/22.
 */

@RestController
@RequestMapping(value = "/lihe/userAccount/login")
public class LoginController extends BaseController {

    private static final Logger L   =  LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "checkUserLogin",method = {RequestMethod.POST})
    public Msg  userLogin(@RequestBody UserLoginEvent event) throws Exception{
        if(Strings.isNullOrEmpty(event.getPassword())||Strings.isNullOrEmpty(event.getTelephone()))
        {
            return new Msg(1,"帐号密码不能为空");
        }
        Msg msg =loginService.checkUserLogin(event.getTelephone(),event.getPassword());
        return  msg;
    }

    /**
     * 获取用户信息
     * @param event
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "getUserInfo",method = {RequestMethod.POST})
    public Msg  getUserInfo(@RequestBody QueryUserInfoEvent event) throws Exception{
        if(event.isCheck())
        {
            return new Msg(1,"参数有误");
        }
        Msg msg =userService.checkToken(event.getUser_tid(),event.getToken());
        if(msg.getCode() !=Constant.SUCCESS)
            return  msg;
         msg =loginService.getUserInfo(event.getUser_tid());
        return  msg;
    }


    /**
     * 验证用户密码
     */
    @RequestMapping(value = "checkUserPwd",method = {RequestMethod.POST})
    public Msg  checkUserPwd(@RequestBody CheckUserPwdEvent event)throws Exception{
        if(event.isCheck())
            return  new Msg(Constant.FAIL,"参数有误");
          boolean isCheck =loginService.checkUserPwd(event);
        Msg msg =new Msg();
        msg.setCode(Constant.SUCCESS);
        msg.setMsg(Constant.SUCCESS_MSG);
        msg.setData(isCheck);
        return  msg;
    }

    /**
     * 修改用户密码 根据旧密码
     * @param event
     * @return
     */
    @RequestMapping(value = "fixUsePwd",method = {RequestMethod.POST})
    public Msg  fixUserPwd(@RequestBody FixUserPwdEvent event)throws Exception{
        if(event.isCheck())
            return  new Msg(Constant.FAIL,"参数有误");
        return  loginService.fixUserPwd(event);
    }


    /**
     * 重置密码
     * @param event
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "resetUserPwd",method = {RequestMethod.POST})
    public Msg  resetUserPwd(@RequestBody ResetUserPwdEvent event) throws Exception{
        if(event.isCheck())
            return  new Msg(Constant.FAIL,"参数有误");
        Msg msg =new Msg();
       return  loginService.resetUserPwd(event);
    }



    /**
     * 修改用户交易密码 根据旧密码
     * @param event
     * @return
     */
    @RequestMapping(value = "fixTradePwd",method = {RequestMethod.POST})
    public Msg  fixUserPwd(@RequestBody FixTradePwdEvent event)throws Exception{
        if(event.isCheck())
            return  new Msg(Constant.FAIL,"参数有误");
         Msg msg =userService.checkToken(event.getUser_tid(),event.getToken());
        if(msg.getCode() !=Constant.SUCCESS)
            return  msg;
        msg=loginService.fixTradePwd(event);
        return msg;
    }


    /**
     * 修改用户交易密码 根据旧密码
     * @param event
     * @return
     */
    @RequestMapping(value = "resetTradePwd",method = {RequestMethod.POST})
    public Msg  resetTradePwd(@RequestBody ResetTradePwdEvent event)throws Exception{
        L.info("reset trade pwd param is "+event.toString());
        if(event.isCheck())
            return  new Msg(Constant.FAIL,"参数有误");
        Msg msg =userService.checkToken(event.getUser_tid(),event.getToken());
        if(msg.getCode() !=Constant.SUCCESS)
            return  msg;
        msg=loginService.resetTradePwd(event);
        return msg;
    }

    /***
     * 获取安全中心信息
     */
    @RequestMapping(value = "getSafeInfo",method = {RequestMethod.POST})
    public Msg  getSafeInfo(@RequestBody QuerySafeInfoEvent event)throws Exception{
        if(event.isCheck())
            return  new Msg(Constant.FAIL,"参数有误");
        Msg msg =userService.checkToken(event.getUser_tid(),event.getToken());
        if(msg.getCode() !=Constant.SUCCESS)
            return  msg;
        msg=loginService.getSafeInfo(event);
        return msg;
    }







}
