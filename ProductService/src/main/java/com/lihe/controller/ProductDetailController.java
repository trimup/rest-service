package com.lihe.controller;

import com.lihe.common.Constant;
import com.lihe.common.Msg;
import com.lihe.event.*;
import com.lihe.persistence.FundNetDayMapper;
import com.lihe.service.ProductDetailService;
import com.lihe.service.UserService;
import com.lihe.until.AccountDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by trimup on 2016/8/23.
 */
@RestController
@RequestMapping(value = "/lihe/product/productDetail")
public class ProductDetailController {

    private static final Logger L = LoggerFactory.getLogger(ProductDetailController.class);

    private static final SimpleDateFormat Format = new SimpleDateFormat("yyyyMMddHHmmss");



    @Autowired
    private ProductDetailService productDetailService;

    @Autowired
    private FundNetDayMapper fundNetDayMapper ;

    @Autowired
    private UserService userService;

    /**
     * 获取公募基金信息
     * @param event
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "getPubProInfo" ,method = RequestMethod.POST)
    public Msg getPubProInfo(@RequestBody QueryPubProDetailEvent event) throws Exception{
        if(event.isCheck())
            return new Msg(Constant.FAIL,"参数有误");
        Msg msg  =productDetailService.getPubProInfo(event.getFund_code());
        return  msg;
    }

    /**
     * 获取公募基金净值趋势信息
     * @param event
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "getFundTrend" ,method = RequestMethod.POST)
    public Msg getFundTrend(@RequestBody QueryFundTrendEvent event) throws Exception{
        if(event.isCheck())
            return new Msg(Constant.FAIL,"参数有误");
        Msg msg  =productDetailService.getFundTrend(event.getFund_code(),event.getTime_limit());
        return  msg;
    }


    /**
     * 获取公募基金历史净值
     * @param event
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "getFundHistoryNet" ,method = RequestMethod.POST)
    public Msg getFundHistoryNet(@RequestBody QueryPubProDetailEvent event) throws Exception{
        if(event.isCheck())
            return new Msg(Constant.FAIL,"参数有误");
        FundHistoryNetData data  =productDetailService.getFundHistoryNet(event);
        Msg msg =new Msg();
        msg.setCode(Constant.SUCCESS);
        msg.setData(data);
        msg.setMsg(Constant.SUCCESS_MSG);
        return  msg;
    }


    /**
     * 获取公募基金基本信息
     * @param event
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "getPubProBaseDetail" ,method = RequestMethod.POST)
    public Msg getPubProBaseDetail(@RequestBody QueryPubProDetailEvent event) throws Exception{
        if(event.isCheck())
            return new Msg(Constant.FAIL,"参数有误");
        Msg msg  =productDetailService.getPubProBaseDetail(event.getFund_code());
        return  msg;
    }

    /**
     * 获取公募基金基金经理信息
     */
    @RequestMapping(value = "getPubProFundMananger" ,method = RequestMethod.POST)
    public Msg getPubProFundMananger(@RequestBody QueryPubProDetailEvent event) throws Exception{
        if(event.isCheck())
            return new Msg(Constant.FAIL,"参数有误");
        Msg msg  =productDetailService.getPubProFundMananger(event.getFund_code());
        return  msg;
    }


    /**
     * 获取公募基金费率信息
     */
    @RequestMapping(value = "getPubProFundRate" ,method = RequestMethod.POST)
    public Msg getPubProFundRate(@RequestBody QueryFundRateEvent event) throws Exception{
        if(event.isCheck())
            return new Msg(Constant.FAIL,"参数有误");
        Msg msg  =productDetailService.getPubProFundRate(event.getFund_code());
        return  msg;
    }

    /**
     * 增加测试数据  基金每日净值
     */
    @RequestMapping(value = "addFundNetDay" ,method = RequestMethod.GET)
    public Msg addFundNetDay(String fund_code) throws Exception{
        List<String> dateList = AccountDate.getEveryday("2015-08-29","2016-08-29");
        for(String d: dateList)
            fundNetDayMapper.insertRateValue(d,fund_code);
        return  new Msg(0,"完成");
    }


    /**
     * 持有基金详情
     */
    @RequestMapping(value = "holdFundDetail" ,method = RequestMethod.POST)
    public Msg holdFundDetail(@RequestBody QueryHoldFundEvent event) throws Exception{

        if(event.isCheck())
            return  new Msg(Constant.FAIL,"参数有误");
        Msg msg =userService.checkToken(event.getUser_tid(),event.getToken());
        if(msg.getCode() !=Constant.SUCCESS)
            return  msg;
        msg = productDetailService.holdFundDetail(event.getFund_code(),event.getUser_tid());
        return msg;
    }

    /**
     * 申请中基金详情()
     */
    @RequestMapping(value = "applyingFundDetail" ,method = RequestMethod.POST)
    public Msg applyingFundDetail(@RequestBody QueryApplyFundEvent event) throws Exception{

        if(event.isCheck())
            return  new Msg(Constant.FAIL,"参数有误");
        Msg msg =userService.checkToken(event.getUser_tid(),event.getToken());
        if(msg.getCode() !=Constant.SUCCESS)
            return  msg;
        msg = productDetailService.applyingFundDetail(event.getFund_code(),event.getUser_tid(),event.getAllot_no());
        return msg;
    }


    /**
     * 认购基金详情
     */
    @RequestMapping(value = "subsFundDetail" ,method = RequestMethod.POST)
    public Msg subsFundDetail(@RequestBody QueryHoldFundEvent event) throws Exception{

        if(event.isCheck())
            return  new Msg(Constant.FAIL,"参数有误");
        Msg msg =userService.checkToken(event.getUser_tid(),event.getToken());
        if(msg.getCode() !=Constant.SUCCESS)
            return  msg;
        msg = productDetailService.subsFundDetail(event.getFund_code(),event.getUser_tid());
        return msg;
    }

    /**
     * 增加测试数据  基金每日净值
     */
    @RequestMapping(value = "addSunFundNetDay" ,method = RequestMethod.GET)
    public Msg addSunNetDay(String product_id) throws Exception{
        List<String> dateList = AccountDate.getEveryday("2015-09-13","2016-08-29");
        for(String d: dateList)
            fundNetDayMapper.insertValue(d,product_id);
        return  new Msg(0,"完成");
    }





    /**
     * 获取公募基金的赎回费率信息
     * @param event
     *          基金编码
     * @return
     */
    @RequestMapping(value = "/queryPubFundRateForRedeem", method = RequestMethod.POST)
    public Msg queryPubFundRateForRedeem (@RequestBody QueryPubFundRateForRedeemEvent event) throws Exception {
        if(event.isCheck())
            return new Msg(Constant.FAIL,"参数有误");
        Msg msg  = productDetailService.queryPubFundRateForRedeem(event.getFund_code());
        return  msg;
    }
}


