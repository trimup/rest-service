package com.lihe.controller;



import com.lihe.Event.QueryNoticeEvent;
import com.lihe.common.Constant;
import com.lihe.common.Msg;
import com.lihe.service.NoticeService;
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
 * Created by Administrator on 2015/11/11.
 * 消息中心
 */
@RestController
@RequestMapping(value = "/lihe/interactive/messageCenter")
public class MessageCenterController {
    @Autowired
    private UserService userService;
    @Autowired
    private NoticeService noticeService;

    private static final SimpleDateFormat Format = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final Logger L = LoggerFactory.getLogger(MessageCenterController.class);



    @RequestMapping(value = "queryNotice",method = RequestMethod.POST)
    public Msg queryNotice(@RequestBody QueryNoticeEvent event){
        if(event.isCheck())
            return  new Msg(Constant.FAIL,"参数有误");
        Msg msg =userService.checkToken(event.getUser_tid(),event.getToken());
        if(msg.getCode() !=Constant.SUCCESS)
            return  msg;
        msg.setCode(Constant.SUCCESS);
        msg.setMsg(Constant.SUCCESS_MSG);
        msg.setData(noticeService.queryNoticeList(event));
        return  msg;

    }



}
