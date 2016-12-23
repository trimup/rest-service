/**
 * @Version 1.0.0
 * Copyright (c) 2016上海相诚金融-版权所有
 */
package com.lihe.service;

import com.lihe.AppConfig;
import com.lihe.common.Constant;
import com.lihe.common.Msg;
import com.lihe.common.TradeEnums;
import com.lihe.entity.*;
import com.lihe.event.hsOrder.HSOrderListQueryEvent;
import com.lihe.event.hsOrder.QueryHSOrderListEvent;
import com.lihe.event.hsUserFund.HsUserFundForRedeemQueryEvent;
import com.lihe.event.hsUserFund.QueryHsUserFundForRedeemEvent;
import com.lihe.persistence.*;
import com.lihe.pojo.HSOrderPojo;
import com.lihe.pojo.UserInvestListPojo;
import com.lihe.replay.hsTrade.*;
import com.lihe.util.DateUtil;
import com.lihe.util.InterfaceCallingUtil;
import com.lihe.util.StringQedUtil;
import org.aspectj.weaver.patterns.HasMemberTypePattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Class HsUserFundInfoService
 * @Description
 * @Author 张超超
 * @Date 2016/9/14 10:00
 */
@Service
public class HSUserFundInfoService {

    private Logger L = LoggerFactory.getLogger(HSUserFundInfoService.class);

    @Autowired
    private HSProductMapper hsProductMapper;

    @Autowired
    private FundInfoMapper fundInfoMapper;
    @Autowired
    private UserBankAccountMapper userBankAccountMapper;

    @Autowired
    private AppConfig appConfig;

    /**
     * 用户基金数据查询（赎回页面用）
     * @param req
     *          参数
     * @return
     */
    public Msg queryHsUserFundForRedeem(QueryHsUserFundForRedeemEvent req) {
        Msg msg = new Msg();
        msg.setCode(Constant.FAIL);

        if (null == req.getUserId()) {
            msg.setMsg("用户ID不能为空");
            return msg;
        }

        if (null == req.getProductId()) {
            msg.setMsg("基金ID不能为空");
            return msg;
        }

        if (null == req.getBankAccounId()) {
            msg.setMsg("银行账号ID不能为空");
            return msg;
        }

        HSProductInfo hsProductInfo = hsProductMapper.findHSProductInfoById(req.getProductId());
        if (null == hsProductInfo) {
            msg.setMsg("基金不存在");
            return msg;
        }

        UserBankAccount bankAccount = userBankAccountMapper.findUserBankAccountById(req.getBankAccounId());
        if (null == bankAccount) {
            msg.setMsg("查询不到银行账号");
            return msg;
        }
        String trade_acco = bankAccount.getTrade_acco();

        HsUserFundForRedeemQueryEvent event = new HsUserFundForRedeemQueryEvent();
        event.setProductName(hsProductInfo.getFund_name());

        BigDecimal netValue = hsProductInfo.getNet_value(); //净值
        Integer navDate = hsProductInfo.getNav_date(); //净值日期
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("trade_acco", trade_acco);
        paramMap.put("fund_code", hsProductInfo.getFund_code());
        paramMap.put("isFilter", "1");
        ShareQueryReply reply =shareQueryApply(paramMap);
        if(!reply.getCode().equals(Constant.HS_SUCCESS_CODE)){
            msg.setMsg("系统异常");
            return msg;
        }
        if(reply.getShareQuerys()==null||reply.getShareQuerys().isEmpty()){
            msg.setMsg("用户尚未持有该基金");
            return  msg;
        }
        ShareQueryPo shareQueryPo =reply.getShareQuerys().get(0);

        event.setEnableShares(shareQueryPo.getEnable_shares());
        event.setTotalShares(shareQueryPo.getCurrent_share());
        event.setTotalAssets(shareQueryPo.getEnable_shares().multiply(shareQueryPo.getNet_value()));
        int offDay = getOffDay(hsProductInfo.getFund_code(), 1);

        Date expectedDate = getExpectedDate(new Date(), offDay);
        event.setExpectedDateStr(DateUtil.dateToString(expectedDate, "yyyy-MM-dd"));
        event.setExpectedDateStrFormart(DateUtil.dateToString(expectedDate, "yyyy年MM月dd日"));

        msg.setCode(Constant.SUCCESS);
        msg.setData(event);
        msg.setMsg("数据查询成功");
        return msg;
    }

    /**
     * 获取T+N
     * @param fundCode
     *          基金代码
     * @param type
     *          类型(0:申购；1：赎回)
     * @return
     */
    public int getOffDay(String fundCode, int type) {
        //从lh_fund_info表中获取 redeem_confir_time 字段（T+n)
        int offDay = 1;
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("fund_code", fundCode);
        List<FundInfo> fundInfoList = fundInfoMapper.findFundInfoListByParams(paramMap);
        if (fundInfoList.size() > 0) {
            String confirTime = fundInfoList.get(0).getPu_confir_time();
            if (0 == type) {
                confirTime = fundInfoList.get(0).getPu_confir_time();
            } else if (1 == type) {
                confirTime = fundInfoList.get(0).getRedeem_confir_time();
            }
            if (StringQedUtil.isNotBlank(confirTime)) {
                if (confirTime.matches("T\\+[0-9]{1,2}")) {
                    confirTime = confirTime.replace("T+", "");
                    offDay = Integer.parseInt(confirTime);
                }
            }
        }
        return offDay;
    }

    /**
     * 查询收益日期(T+n)
     * @param date
     * 			起始日期
     * @param offDay
     * 			n (T+n)
     * @return
     */
    private static Date getExpectedDate(Date date, int offDay) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        //下午三点，第二个交易日
        if (c.get(Calendar.HOUR_OF_DAY) == 15) {
            c.add(Calendar.DAY_OF_MONTH, 1);
        }

        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.MILLISECOND, 0);

        //起始日期处于当前星期的位置索引
        int dayOfWeekBefore = c.get(Calendar.DAY_OF_WEEK);
        //DAY_OF_WEEK数据处理，修改周一为第一天（1），周日为最后一天（7）
        dayOfWeekBefore = dayOfWeekBefore - 1;
        if (0 == dayOfWeekBefore) dayOfWeekBefore = 7;

        //预计日期的相对索引（不考虑周末的情况）
        int dayOfWeekAfter = dayOfWeekBefore + offDay;

        int offDayAdd = 0; //偏差值


        //计算偏差值（周末）
        int offDayFinal = offDay;
        while (dayOfWeekBefore <= dayOfWeekAfter) {
            offDayAdd = 0;
            for (int i = dayOfWeekBefore; i <= dayOfWeekAfter; i++) {
                if (i % 7 == 6 || i % 7 == 0) {
                    offDayAdd++;
                }
            }
            dayOfWeekBefore = dayOfWeekAfter + 1;
            dayOfWeekAfter += offDayAdd;
            offDayFinal += offDayAdd;
        }

        //获取最终预计日期
        c.add(Calendar.DAY_OF_MONTH, offDayFinal);
        Date expectedDate = c.getTime();

        return expectedDate;
    }


    /**
     * 获取预计交易确认时间
     * @param fundCode
     *          基金代码
     * @param type
     *          交易类型(0:申购；1：赎回)
     * @return
     */
    public Date getExpectedDateForTrade(String fundCode, int type) {

        int offDay = getOffDay(fundCode, type);

        Date expectedDate = getExpectedDate(new Date(), offDay);

        return expectedDate;
    }



    /**
     * 购买申请查询
     * @param paramMap
     *          参数集合
     * @return
     */
    private TradeApplyQueryApply tradeApplyQueryApply(Map<String, String> paramMap) {
        String url = appConfig.getURL_HS_TRADEAPPLYQUERY();
        url = MessageFormat.format(url, appConfig.getIP_PORT());
        TradeApplyQueryApply apply = InterfaceCallingUtil.call(url, paramMap, InterfaceCallingUtil.POST, 0,
                TradeApplyQueryApply.class);
        return apply;
    }


    /**
     * 份额查询
     * @param paramMap
     *          参数集合
     * @return
     */
    private ShareQueryReply shareQueryApply(Map<String, String> paramMap) {
        String url = appConfig.getURL_HS_SHARE_QUERY();
        url = MessageFormat.format(url, appConfig.getIP_PORT());
        ShareQueryReply apply = InterfaceCallingUtil.call(url, paramMap, InterfaceCallingUtil.POST, 0,
                ShareQueryReply.class);
        return apply;
    }



}
