package com.lihe.controller;

import com.lihe.common.Constant;
import com.lihe.common.Msg;
import com.lihe.service.AdsCfgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by trimup on 2016/8/18.
 * 广告配置
 */
@RestController
@RequestMapping(value = "/lihe/interactive/adscfg")
public class AdsCfgController {

    private static final Logger L = LoggerFactory.getLogger(AdsCfgController.class);


    @Autowired
    private AdsCfgService adsCfgService;

    /**'
     * 获取首页广告配置信息
     */
    @RequestMapping(value = "/getHomeAdsCfg" ,method = {RequestMethod.GET,RequestMethod.POST})
    public Msg getHomeAdsCfg(){
        Msg msg =new Msg();
        msg.setCode(Constant.SUCCESS);
        msg.setData(adsCfgService.getHomeAdsCfg());
        msg.setMsg(Constant.SUCCESS_MSG);
        return  msg;

    }




}
