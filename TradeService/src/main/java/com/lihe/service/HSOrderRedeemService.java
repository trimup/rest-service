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
import com.lihe.event.hsTrade.RedeemProductEvent;
import com.lihe.persistence.*;
import com.lihe.replay.hsTrade.*;
import com.lihe.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Class HSOrderRedeemService
 * @Description
 * @Author 张超超
 * @Date 2016/9/12 17:28
 */
@Service
public class HSOrderRedeemService {

    private Logger L = LoggerFactory.getLogger(HSOrderPurchaseService.class);

    private static final SimpleDateFormat Format = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat Time_Format = new SimpleDateFormat("yyyyMMdd");


    @Autowired
    private HSOrderRedeemMapper hsOrderRedeemMapper;

    @Autowired
    private HSProductMapper hsProductMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserBankAccountMapper userBankAccountMapper;

    @Autowired
    private HSUserFundInfoMapper hsUserFundInfoMapper;

    private AppConfig appConfig;


    /**
     * 赎回基金
     * @param req
     *          参数
     * @return
     */
    @Transactional
    public Msg redeemProduct(RedeemProductEvent req) throws ParseException {
        Msg msg = new Msg();
        //非空校验
        msg = validateEmpty(req);
        if (Constant.FAIL == msg.getCode()) {
            return msg;
        }
        msg.setCode(Constant.FAIL);

        //token validate
        UserInfo userInfo = userInfoMapper.findUserById(req.getUserId());
        if (null == userInfo) {
            msg.setMsg("无法查询到该用户");
            return msg;
        }
        if (!userInfo.getToken().equals(req.getToken().trim())) {
            msg.setCode(Constant.REPEAT_LOGIN);
            msg.setMsg("此账户长时间未进行操作或已通过其他设备登录，请退出系统后重新登录");
            return msg;
        }
        String password = "";
        String passwordMD5 = "";
        try {
            password = DESUntil.decode(appConfig.getDES_KEY(), Base64.decode(userInfo.getTrade_password()));
            passwordMD5 = MD5.sign(password, "utf-8");
        } catch (Exception e) {
            L.info("password decode error : " + e);
            msg.setMsg("解析密码错误");
            return msg;
        }
        if (!passwordMD5.equals(req.getPassword())) {
            msg.setMsg("支付密码错误");
            msg.setCode(Constant.PSSWORD_ERR);
            return msg;
        }

        HSProductInfo productInfo = hsProductMapper.findHSProductInfoById(req.getHsProductId());
        if (null == productInfo) {
            msg.setMsg("无法查询到该基金");
            return msg;
        }
//        if (!TradeEnums.HSFundStatusEnum.ZC.getValue().equals(productInfo.getFund_status())
//                && !TradeEnums.HSFundStatusEnum.FX.getValue().equals(productInfo.getFund_status())) {
//            msg.setMsg("当前基金无法购买");
//            return msg;
//        }
        String fund_code = productInfo.getFund_code();
        char share_type = productInfo.getShare_type();


        UserBankAccount bankAccount = userBankAccountMapper.findUserBankAccountById(req.getBankAccounId());
        String trade_acco = bankAccount.getTrade_acco();
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("shares", StringQedUtil.getFormatString(req.getShares().doubleValue()));
        paramMap.put("fund_code", fund_code);
        paramMap.put("password", password);
        paramMap.put("share_type", String.valueOf(share_type));
        paramMap.put("trade_acco", trade_acco);

        String allot_no = "";
        //赎回基金
        RedeemApply apply =  redeemApply(paramMap);
        if (null != apply) {
            if (TradeEnums.HSTradeResultStatusEnum.CG.getValue().equals(apply.getCode())) {
                allot_no = apply.getAllot_no();
            } else {
                msg.setMsg(apply.getMessage());
                return msg;
            }
        } else {
            msg.setMsg("赎回基金失败");
            return msg;
        }

        //添加赎回记录
        HSOrderRedeem orderRedeem = addOrderRedeemWithParams(req.getUserId(), req.getHsProductId(), req.getShares(), fund_code,
                share_type, trade_acco, allot_no);

        Map<String, String> resultParamMap = new HashMap<>();
        resultParamMap.put("allot_no", allot_no);
        resultParamMap.put("trade_acco", trade_acco);
        //申请结果查询
        TradeResultQueryApply resultapply = tradeResultQueryApply(resultParamMap);
        HashMap<String, String> resultMap = new HashMap<>();
        if (TradeEnums.HSTradeResultStatusEnum.CG.getValue().equals(apply.getCode())) {
            if (resultapply.getTradeResultQuerys().length > 0) {
                TradeResultQueryPo resultQueryPo = resultapply.getTradeResultQuerys()[0];
                if (TradeEnums.HSTradeStatusCharacterEnum.QRSB.getValue().equals(resultQueryPo.getTrade_status())) {
                    msg.setCode(Constant.FAIL);
                    msg.setMsg("赎回基金失败");
                    return  msg;
                }else{
                    resultMap.put("confirmDate",Format.format(Time_Format.parse(resultQueryPo.getAffirm_date().toString())) );
                }
            }
        }


        msg.setCode(Constant.SUCCESS);
        msg.setMsg("基金赎回申请已提交");
        return msg;
    }

    /**
     * 添加赎回记录
     * @param userId
     *          用户ID
     * @param hsProductId
     *          基金ID
     * @param shares
     *          份额
     * @param fund_code
     *          基金代码
     * @param share_type
     *          类型
     * @param trade_acco
     *          账号
     * @param allot_no
     *          交易流水号
     * @return
     */
    private HSOrderRedeem addOrderRedeemWithParams(Integer userId, Integer hsProductId, BigDecimal shares, String fund_code,
                               char share_type, String trade_acco, String allot_no) {
        HSOrderRedeem orderRedeem = new HSOrderRedeem();
        orderRedeem.setUser_tid(userId);
        orderRedeem.setHs_product_tid(hsProductId);
        orderRedeem.setAllot_no(allot_no);
        orderRedeem.setFund_code(fund_code);
        orderRedeem.setShare_type(share_type);
        orderRedeem.setTrade_acco(trade_acco);
        orderRedeem.setShares(shares);
        orderRedeem.setTrade_status_tid(TradeEnums.HSTradeStatusEnum.DQR.getValue());
        orderRedeem.setTrade_status(TradeEnums.HSTradeStatusCharacterEnum.DQR.getValue());
        orderRedeem.setCreate_time(new Date());
        hsOrderRedeemMapper.addHsOrderRedeem(orderRedeem);
        return orderRedeem;
    }

    /**
     * 非空校验
     * @param req
     *          参数
     * @return
     */
    private Msg validateEmpty(RedeemProductEvent req) {
        Msg msg = new Msg();
        msg.setCode(Constant.FAIL);

        if (null == req) {
            msg.setMsg("参数不能为空");
        } if (null == req.getUserId()) {
            msg.setMsg("用户ID不能为空");
        } else if (StringQedUtil.isBlank(req.getToken())) {
            msg.setMsg("token不能为空");
        } else if (StringQedUtil.isBlank(req.getPassword())) {
            msg.setMsg("密码不能为空");
        } else if (null == req.getHsProductId()) {
            msg.setMsg("基金ID不能为空");
        } else if (null == req.getBankAccounId()) {
            msg.setMsg("用户银行账户ID不能为空");
        } else if (null == req.getShares()) {
            msg.setMsg("赎回份额不能为空");
        } else {
            msg.setCode(Constant.SUCCESS);
        }
        return msg;
    }

    /**
     * 交易请求查询处理
     * @param orderId
     *          基金交易订单ID
     * @return
     */
    @Transactional
    public Msg handlerOrderRedeemApply(int orderId) {
        Msg msg = new Msg();
        msg.setCode(Constant.FAIL);

        HSOrderRedeem orderRedeem = hsOrderRedeemMapper.findHsOrderRedeemById(orderId);
        if (null == orderRedeem) {
            msg.setMsg("无法查询到订单");
            return msg;
        }

        if (TradeEnums.HSTradeStatusEnum.DQR.getValue() != orderRedeem.getTrade_status_tid()) {
            msg.setMsg("订单已不属于待处理状态");
            return msg;
        }

        String allot_no = orderRedeem.getAllot_no();
        String businflag = TradeEnums.HSBusinessApplyCodeEnum.SH.getValue(); //赎回业务
        int sort_way = 0;
        String trade_acco = orderRedeem.getTrade_acco();

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("allot_no", allot_no);
        paramMap.put("businflag", businflag);
        paramMap.put("sort_way", String.valueOf(sort_way));
        paramMap.put("trade_acco", trade_acco);

        //申请结果查询
        TradeApplyQueryApply apply = tradeApplyQuery(paramMap);
        if (null == apply) {
            msg.setMsg("交易结果查询失败");
            return msg;
        }
        if (TradeEnums.HSTradeResultStatusEnum.CG.getValue().equals(apply.getCode())) {
            if (apply.getTradeApplyQuerys().length > 0) {
                TradeApplyQueryPo queryRequestPo =  apply.getTradeApplyQuerys()[0];
//            TradeApplyQueryPo queryRequestPo = new TradeApplyQueryPo();
//            queryRequestPo.setConfirm_flag('9');
//            queryRequestPo.setShares(new BigDecimal("1234"));
//            queryRequestPo.setBalance(new BigDecimal("50000"));
                if (TradeEnums.HSTradeStatusCharacterEnum.DQR.getValue().equals(queryRequestPo.getConfirm_flag())) {
                    orderRedeem.setApply_shares(queryRequestPo.getShares());
                    orderRedeem.setApply_balance(queryRequestPo.getBalance());
                    //购买表数据修改
                    hsOrderRedeemMapper.updateHsOrderRedeemApplyData(orderRedeem.getId(),
                            orderRedeem.getApply_shares(), orderRedeem.getApply_balance());

                    //用户基金表修改
                    msg = updateUserFundData(orderRedeem, 4);
                    if (Constant.FAIL == msg.getCode()) {
                        return  msg;
                    }
                    msg.setCode(Constant.SUCCESS);
                    msg.setMsg("基金赎回申请已提交");
                } else {
                    msg.setMsg("交易已不属于待处理状态");
                    return msg;
                }
            } else {
                msg.setMsg("交易结果查询为空");
                return msg;
            }
        } else {
            msg.setMsg("交易结果查询失败");
            return msg;
        }

        return msg;
    }

    /**
     * 交易结果查询处理
     * @param orderId
     *          基金交易订单ID
     * @return
     */
    @Transactional
    public Msg handlerTradeResultQuery(int orderId) {
        Msg msg = new Msg();
        msg.setCode(Constant.FAIL);

        HSOrderRedeem orderRedeem = hsOrderRedeemMapper.findHsOrderRedeemById(orderId);
        if (null == orderRedeem) {
            msg.setMsg("无法查询到订单");
            return msg;
        }
        HSProductInfo productInfo = hsProductMapper.findHSProductInfoById(orderRedeem.getHs_product_tid());

        if (TradeEnums.HSTradeStatusEnum.DQR.getValue() != orderRedeem.getTrade_status_tid()) {
            msg.setMsg("订单状态不正确");
            return msg;
        }

        String allot_no = orderRedeem.getAllot_no();
        String businflag = TradeEnums.HSBusinessApplyCodeEnum.SH.getValue(); //赎回业务
        int sort_way = 0;
        String trade_acco = orderRedeem.getTrade_acco();

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("allot_no", allot_no);
        paramMap.put("businflag", businflag);
        paramMap.put("sort_way", String.valueOf(sort_way));
        paramMap.put("trade_acco", trade_acco);

        //交易结果查询
        TradeResultQueryApply result = tradeResultQueryApply(paramMap);
        if (null == result) {
            msg.setMsg("交易结果查询失败");
            return msg;
        }
        if (TradeEnums.HSTradeResultStatusEnum.CG.getValue().equals(result.getCode())) {
            if (result.getTradeResultQuerys().length > 0) {
                TradeResultQueryPo queryResultPo =  result.getTradeResultQuerys()[0];

                orderRedeem.setTrade_confirm_shares(queryResultPo.getTrade_confirm_shares());
                orderRedeem.setTrade_confirm_balance(queryResultPo.getTrade_confirm_balance());
                if (null != queryResultPo.getAffirm_date()) {
                    orderRedeem.setAffirm_date(queryResultPo.getAffirm_date());
                    orderRedeem.setConfirm_date(DateUtil.stringToDate(String.valueOf(queryResultPo.getAffirm_date()), "yyyyMMdd"));
                }
                orderRedeem.setTrade_status(queryResultPo.getTrade_status());

                Integer type = null;

                if (TradeEnums.HSTradeStatusCharacterEnum.DQR.getValue().equals(orderRedeem.getTrade_status())) {
                    if (TradeEnums.HSTradeStatusCharacterEnum.QRCG.getValue().equals(queryResultPo.getTrade_status())) {
                        //确认成功
                        orderRedeem.setTrade_status_tid(TradeEnums.HSTradeStatusEnum.QRCG.getValue());
                        type = 12;
                    } else if (TradeEnums.HSTradeStatusCharacterEnum.BFQR.getValue().equals(queryResultPo.getTrade_status())) {
                        //部分确认
                        orderRedeem.setTrade_status_tid(TradeEnums.HSTradeStatusEnum.BFQR.getValue());
                        type = 12;
                    } else if (TradeEnums.HSTradeStatusCharacterEnum.QRSB.getValue().equals(queryResultPo.getTrade_status())) {
                        //确认失败
                        orderRedeem.setTrade_status_tid(TradeEnums.HSTradeStatusEnum.QRSB.getValue());
                        type = 5;
                    }
//                    else {
//                        //默认为失败
//                        orderPurchase.setTrade_status_tid(TradeEnums.HSTradeStatusEnum.QRSB.getValue());
//                        type = 1;
//                    }
                }  else {
                    msg.setMsg("交易已不属于待处理状态");
                    return msg;
                }

                if (null != type) {
                    //基金赎回表数据修改
                    hsOrderRedeemMapper.updateHsOrderRedeemTradeData(orderRedeem.getId(),
                            orderRedeem.getTrade_confirm_shares(), orderRedeem.getTrade_confirm_balance(),
                            orderRedeem.getTrade_status(), orderRedeem.getTrade_status_tid(),
                            orderRedeem.getAffirm_date(), orderRedeem.getConfirm_date(), orderRedeem.getFare_sx());

//                    msg = updateUserFundData(orderRedeem, type);
                    if (Constant.FAIL == msg.getCode()) {
                        return  msg;
                    }
                    msg.setCode(Constant.SUCCESS);
                    msg.setData("交易处理完成");
                } else {
                    msg.setData("交易处理没有匹配的状态");
                }

            }
        } else {
            msg.setMsg("交易结果查询失败");
            return msg;
        }

        return msg;
    }

    /**
     * 修改用户基金表的份额、金额数据
     * @param orderRedeem
     *          用户基金数据
     * @param type
     *          操作类型（4：赎回中；5：赎回失败；12：赎回成功）
     * @return
     */
    private Msg updateUserFundData(HSOrderRedeem orderRedeem, int type) {
        Msg msg = new Msg();
        msg.setCode(Constant.FAIL);

        List<HSUserFundInfo> userFundList =  hsUserFundInfoMapper
                .findUserFundListByParams(orderRedeem.getUser_tid(), orderRedeem.getTrade_acco(),
                        orderRedeem.getHs_product_tid());

        if (userFundList.size() > 0) {
            HSUserFundInfo userFundInfo = userFundList.get(0);
            if (4 == type || 5 == type) { //赎回中、赎回失败
                hsUserFundInfoMapper.updateHSUserFundInfo1(orderRedeem.getApply_shares(),
                        orderRedeem.getApply_balance(), userFundInfo.getId(), type);
                msg.setCode(Constant.SUCCESS);
                msg.setMsg("用户基金表数据同步完成");
            } else if (12 == type) { //赎回成功
                hsUserFundInfoMapper.updateHSUserFundInfo2(orderRedeem.getTrade_confirm_shares(),
                        orderRedeem.getApply_shares(), orderRedeem.getApply_balance(), userFundInfo.getId(), type);
                msg.setCode(Constant.SUCCESS);
                msg.setMsg("用户基金表数据同步完成");
            } else {
                msg.setMsg("没有对应的type");
            }
        } else {
            msg.setMsg("查询不到用户基金表信息");
        }
        return msg;
    }

    /**
     * 赎回
     * @param paramMap
     *          参数集合
     * @return
     */
    private RedeemApply redeemApply(Map<String, String> paramMap) {
        String url = appConfig.getURL_HS_REDEEM();
        url = MessageFormat.format(url, appConfig.getIP_PORT());
        RedeemApply apply = InterfaceCallingUtil.call(url, paramMap, InterfaceCallingUtil.POST, 0, RedeemApply.class);
        return apply;
    }
    /**
     * 购买结果查询
     * @param paramMap
     *          参数集合
     * @return
     */
    private TradeResultQueryApply tradeResultQueryApply(Map<String, String> paramMap) {
        String url = appConfig.getURL_HS_TRADERESULTQUERY();
        url = MessageFormat.format(url, appConfig.getIP_PORT());
        TradeResultQueryApply apply = InterfaceCallingUtil.call(url, paramMap, InterfaceCallingUtil.POST, 0,
                TradeResultQueryApply.class);
        return apply;
    }

    /**
     * 交易申请查询
     * @param paramMap
     *          参数集合
     * @return
     */
    private TradeApplyQueryApply tradeApplyQuery(Map<String, String> paramMap) {
        String url = appConfig.getURL_HS_TRADEAPPLYQUERY();
        url = MessageFormat.format(url, appConfig.getIP_PORT());
        TradeApplyQueryApply apply = InterfaceCallingUtil.call(url, paramMap, InterfaceCallingUtil.POST, 0,
                TradeApplyQueryApply.class);
        return apply;
    }

    /**
     * 批量查询并修改交易结果
     */
    @Transactional
    public void batchHandlerTradeResultForRedeem() {
        L.info("method batchHandlerTradeResultForRedeem begin ...");
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("trade_status_tid", TradeEnums.HSTradeStatusEnum.DQR.getValue());
        List<HSOrderRedeem> orderRedeemList = hsOrderRedeemMapper.findHsOrderRedeemList(paramMap);
        if (orderRedeemList.size() > 0) {
            orderRedeemList.forEach(order -> handlerTradeResultQuery(order.getId()));
        }
        L.info("method batchHandlerTradeResultForRedeem end ...");
    }
}
