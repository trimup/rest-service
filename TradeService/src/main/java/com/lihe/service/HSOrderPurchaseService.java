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
import com.lihe.event.hsTrade.CalcPurchaseRateEvent;
import com.lihe.event.hsTrade.PurchaseProductEvent;
import com.lihe.persistence.*;
import com.lihe.pojo.CalcRatePojo;
import com.lihe.replay.hsTrade.*;
import com.lihe.util.*;
import com.lihe.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Class HSOrderPurchaseService
 * @Description
 * @Author 张超超
 * @Date 2016/9/6 10:54
 */
@Service
public class HSOrderPurchaseService {

    private Logger L = LoggerFactory.getLogger(HSOrderPurchaseService.class);

    private static final SimpleDateFormat Format = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat Time_Format = new SimpleDateFormat("yyyyMMdd");



    @Autowired
    private HSOrderPurchaseMapper hsOrderPurchaseMapper;

    @Autowired
    private HSProductMapper hsProductMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserBankAccountMapper userBankAccountMapper;

    @Autowired
    private HSUserFundInfoMapper hsUserFundInfoMapper;

    @Autowired
    private HSBankMapper hsBankMapper;

    @Autowired
    private AppConfig appConfig;

    /**
     * 购买基金
     * @param req
     *          参数
     * @return
     */
    @Transactional
    public Msg purchaseProduct( PurchaseProductEvent req) throws Exception {
        Msg msg = new Msg();

        //非空校验
        msg = validateEmpty(req);
        if (Constant.FAIL == msg.getCode()) {
            return msg;
        }
        msg.setCode(Constant.FAIL);

        BigDecimal balance = req.getBalance();

        UserBankAccount bankAccount = userBankAccountMapper.findUserBankAccountById(req.getBankAccounId());
        if (null == bankAccount) {
            msg.setMsg("查询不到银行账号");
            return msg;
        }
        HSBankCodeInfo hsBankCodeInfo =hsBankMapper.queryHsBankCode(bankAccount.getBank_no());
        if(null ==hsBankCodeInfo){
            msg.setMsg("该银行通道已关闭");
        }


        HSProductInfo productInfo = hsProductMapper.findHSProductInfoById(req.getHsProductId());
        if (null == productInfo) {
            msg.setMsg("无法查询到该基金");
            return msg;
        }
        if (!TradeEnums.HSFundStatusEnum.ZC.getValue().equals(productInfo.getFund_status())
                && !TradeEnums.HSFundStatusEnum.FX.getValue().equals(productInfo.getFund_status())) {
            msg.setMsg("当前基金无法购买");
            return msg;
        }
        String fund_code = productInfo.getFund_code();
        char share_type = productInfo.getShare_type(); //前收费

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
            L.info("the des key is "+appConfig.getDES_KEY()+"and trade pwd is "+userInfo.getTrade_password());
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


        String trade_acco = bankAccount.getTrade_acco();

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("balance", StringQedUtil.getFormatString(balance.doubleValue()));
        paramMap.put("fund_code", fund_code);
        paramMap.put("password", password);
        paramMap.put("share_type", String.valueOf(share_type));
        paramMap.put("trade_acco", trade_acco);
        paramMap.put("capital_mode", hsBankCodeInfo.getMoney_type_code()); //资金方式(英文字母l)
        paramMap.put("detail_fund_way", "01"); //资金明细方式

        String allot_no = "";
        int trade_type = -1;
        if (TradeEnums.HSFundStatusEnum.FX.getValue().equals(productInfo.getFund_status())) {
            //认购
            SubscribeApply apply =  subscribeApply(paramMap);
            if (null != apply) {
                if (TradeEnums.HSTradeResultStatusEnum.CG.getValue().equals(apply.getCode())) {
                    allot_no = apply.getAllot_no();
                    trade_type = TradeEnums.HSFundTradeEnum.RG.getValue();
                } else {
                    msg.setMsg(apply.getMessage());
                    return msg;
                }
            } else {
                msg.setMsg("购买基金失败");
                return msg;
            }
        } else if (TradeEnums.HSFundStatusEnum.ZC.getValue().equals(productInfo.getFund_status())) {
            //申购
            PurchaseApply apply =  purchaseApply(paramMap);
            if (null != apply) {
                if (TradeEnums.HSTradeResultStatusEnum.CG.getValue().equals(apply.getCode())) {
                    allot_no = apply.getAllot_no();
                    trade_type = TradeEnums.HSFundTradeEnum.SG.getValue();
                } else {
                    msg.setMsg(apply.getMessage());
                    return msg;
                }
            } else {
                msg.setMsg("购买基金失败");
                return msg;
            }
        } else {
            msg.setMsg("当前基金无法购买");
            return msg;
        }



        //添加购买记录
        HSOrderPurchase orderPurchase = addOrderPurchaseWithPrams(req, balance, fund_code, share_type, trade_acco, allot_no, trade_type);
        //交易结果查询参数
        Map<String, String> resultParamMap = new HashMap<>();
        resultParamMap.put("allot_no", allot_no);
        resultParamMap.put("trade_acco", trade_acco);
        //申请结果查询
        TradeResultQueryApply apply = tradeResultQueryApply(resultParamMap);
        if (null == apply) {
            msg.setCode(Constant.PAYING);
            msg.setMsg("支付中");
            return msg;
        }
        HashMap<String, String> resultMap = new HashMap<>();
        if (TradeEnums.HSTradeResultStatusEnum.CG.getValue().equals(apply.getCode())) {
            if (apply.getTradeResultQuerys().length > 0) {
                TradeResultQueryPo resultQueryPo = apply.getTradeResultQuerys()[0];
                if (TradeEnums.HSDeductStatusEnum.YFS.getValue().equals(resultQueryPo.getDeduct_status())) {
                    msg.setCode(Constant.PAYING); //支付中
                    msg.setMsg("扣款处理中");
                    resultMap.put("allot_no",resultQueryPo.getAllot_no());
                    resultMap.put("trade_acco",resultQueryPo.getTrade_acco());
                    resultMap.put("order_id",orderPurchase.getId().toString());
                } else if (TradeEnums.HSDeductStatusEnum.YX.getValue().equals(resultQueryPo.getDeduct_status())) {
                    //扣款成功
                    resultMap.put("applyDate", Format.format(Time_Format.parse(resultQueryPo.getApply_date().toString()))); //申请时间
                    resultMap.put("confirmDate", Format.format(Time_Format.parse(resultQueryPo.getAffirm_date().toString()))); //确认时间
                    resultMap.put("incomeDate", Format.format(Time_Format.parse(resultQueryPo.getAffirm_date().toString()))); //收益时间
                    int purchaseUpdateResult = hsOrderPurchaseMapper.updateHsOrderPurchaseApplyData(orderPurchase.getId(),
                            resultQueryPo.getApply_share(), resultQueryPo.getBalance(),
                            TradeEnums.HSDeductStatusEnum.YX.getValue(), TradeEnums.HSDeductStatusEnum.YFS.getValue(),
                            TradeEnums.HSTradeStatusCharacterEnum.DQR.getValue());
                    msg.setCode(Constant.SUCCESS);
                    msg.setMsg("基金购买申请已提交");
                } else {
                    msg.setCode(Constant.FAIL);
                    msg.setMsg(resultQueryPo.getCombined_error_info());
                    return msg;
                }
            } else {
                msg.setCode(Constant.PAYING);
                msg.setMsg("支付中");
                return msg;
            }
        }

        msg.setData(resultMap);
        return msg;
    }


    public  Msg purchaseProductResult(String allot_no,String trade_acco,Integer order_id) throws ParseException {
        Msg msg =new Msg();
        msg.setCode(Constant.FAIL);
        Map<String, String> resultParamMap = new HashMap<>();
        resultParamMap.put("allot_no", allot_no);
        resultParamMap.put("trade_acco", trade_acco);
        //申请结果查询
        TradeResultQueryApply apply = tradeResultQueryApply(resultParamMap);
        if (null == apply) {
            msg.setMsg("交易结果查询失败");
            return msg;
        }
        HashMap<String, String> resultMap = new HashMap<>();
        if (TradeEnums.HSTradeResultStatusEnum.CG.getValue().equals(apply.getCode())) {
            if (apply.getTradeResultQuerys().length > 0) {
                TradeResultQueryPo resultQueryPo = apply.getTradeResultQuerys()[0];
                if (TradeEnums.HSDeductStatusEnum.YFS.getValue().equals(resultQueryPo.getDeduct_status())) {
                    msg.setCode(Constant.PAYING); //支付中
                    msg.setMsg("扣款处理中");
                    resultMap.put("allot_no",resultQueryPo.getAllot_no());
                    resultMap.put("trade_acco",resultQueryPo.getTrade_acco());
                } else if (TradeEnums.HSDeductStatusEnum.YX.getValue().equals(resultQueryPo.getDeduct_status())) {
                    //扣款成功
                    resultMap.put("applyDate", Format.format(Time_Format.parse(resultQueryPo.getApply_date().toString()))); //申请时间
                    resultMap.put("confirmDate", Format.format(Time_Format.parse(resultQueryPo.getAffirm_date().toString()))); //确认时间
                    resultMap.put("incomeDate", Format.format(Time_Format.parse(resultQueryPo.getAffirm_date().toString()))); //收益时间
                    int purchaseUpdateResult = hsOrderPurchaseMapper.updateHsOrderPurchaseApplyData(order_id,
                            resultQueryPo.getApply_share(), resultQueryPo.getBalance(),
                            TradeEnums.HSDeductStatusEnum.YX.getValue(), TradeEnums.HSDeductStatusEnum.YFS.getValue(),
                            TradeEnums.HSTradeStatusCharacterEnum.DQR.getValue());
                    msg.setCode(Constant.SUCCESS);
                    msg.setMsg("基金购买申请已提交");
                } else {
                    msg.setCode(Constant.FAIL);
                    msg.setMsg(resultQueryPo.getCombined_error_info());
                    return msg;
                }
            } else {
                msg.setCode(Constant.PAYING);
                msg.setMsg("支付中");
                return msg;
            }
        }

        msg.setData(resultMap);
        return msg;

    }



    private HSOrderPurchase addOrderPurchaseWithPrams(@RequestBody PurchaseProductEvent req, BigDecimal balance,
                                                      String fund_code, char share_type, String trade_acco,
                                                      String allot_no, int trade_type) {
        //添加记录
        HSOrderPurchase orderPurchase = new HSOrderPurchase();
        orderPurchase.setAllot_no(allot_no);
        orderPurchase.setTrade_type(trade_type);
        orderPurchase.setBalance(balance);
        orderPurchase.setFund_code(fund_code);
        orderPurchase.setShare_type(share_type);
        orderPurchase.setTrade_acco(trade_acco);
        orderPurchase.setTrade_status_tid(TradeEnums.HSTradeStatusEnum.DQR.getValue());
        orderPurchase.setTrade_status(TradeEnums.HSTradeStatusCharacterEnum.DQR.getValue());
        orderPurchase.setUser_tid(req.getUserId());
        orderPurchase.setHs_product_tid(req.getHsProductId());
        orderPurchase.setCreate_time(new Date());
        orderPurchase.setDeduct_status(TradeEnums.HSDeductStatusEnum.YFS.getValue());
        hsOrderPurchaseMapper.addHsOrderPurchase(orderPurchase);
        return orderPurchase;
    }


//    /**
//     * 交易请求查询处理
//     * @param orderId
//     *          基金交易订单ID
//     * @return
//     */
//    public Msg handlerOrderPurchaseApply(int orderId) {
//        Msg msg = new Msg();
//        msg.setCode(Constant.FAIL);
//
//
//
//        String allot_no = orderPurchase.getAllot_no();
//        String businflag = TradeEnums.HSBusinessApplyCodeEnum.SG.getValue(); //申购业务
//        if (0 == orderPurchase.getTrade_type().intValue()) {
//            businflag = TradeEnums.HSBusinessApplyCodeEnum.SG.getValue(); //申购业务
//        } else if (1 == orderPurchase.getTrade_type().intValue()) {
//            businflag = TradeEnums.HSBusinessApplyCodeEnum.RG.getValue(); //认购业务
//        }
//        int sort_way = 0;
//        String trade_acco = orderPurchase.getTrade_acco();
//
//        Map<String, String> paramMap = new HashMap<>();
//        paramMap.put("allot_no", allot_no);
//        paramMap.put("businflag", businflag);
//        paramMap.put("sort_way", String.valueOf(sort_way));
//        paramMap.put("trade_acco", trade_acco);
//
//        //申请结果查询
//        TradeApplyQueryApply apply = tradeApplyQuery(paramMap);
//        if (null == apply) {
//            msg.setMsg("交易结果查询失败");
//            return msg;
//        }
//        //deduct_status（扣款状态）（2 	有效 | 1 	无效 |3 	已发送扣款指令 ）
//        if (TradeEnums.HSTradeResultStatusEnum.CG.getValue().equals(apply.getCode())) {
//            if (apply.getTradeApplyQuerys().length > 0) {
//                TradeApplyQueryPo queryRequestPo =  apply.getTradeApplyQuerys()[0];
//                if (TradeEnums.HSDeductStatusEnum.YFS.getValue().equals(queryRequestPo.getDeduct_status())) {
//                    msg.setCode(Constant.SUCCESS);
//                    msg.setData("扣款处理中");
//                } else if (TradeEnums.HSDeductStatusEnum.YX.getValue().equals(queryRequestPo.getDeduct_status())) {
//                    //扣款成功
//                    orderPurchase.setApply_shares(queryRequestPo.getShares());
//                    orderPurchase.setApply_balance(queryRequestPo.getBalance());
//                    //购买表数据修改(限定修改前状态）
//                    int purchaseUpdateResult = hsOrderPurchaseMapper.updateHsOrderPurchaseApplyData(orderPurchase.getId(),
//                            orderPurchase.getApply_shares(), orderPurchase.getApply_balance(),
//                            TradeEnums.HSDeductStatusEnum.YX.getValue(), TradeEnums.HSDeductStatusEnum.YFS.getValue(),
//                            TradeEnums.HSTradeStatusCharacterEnum.DQR.getValue());
//                    if (purchaseUpdateResult > 0) {
//                        //用户基金表修改
//                        if (0 == orderPurchase.getTrade_type().intValue()) { //申购
//                            msg = updateUserFundData(orderPurchase, 0);
//                        } else if (1 == orderPurchase.getTrade_type().intValue()) { //认购
//                            msg = updateUserFundData(orderPurchase, 2);
//                        }
//                        if (Constant.FAIL == msg.getCode()) {
//                            return  msg;
//                        }
//                        msg.setCode(Constant.SUCCESS);
//                        msg.setData("基金购买申请已提交");
//                    } else {
//                        msg.setCode(Constant.SUCCESS);
//                        msg.setData("基金购买申请已经提交");
//                    }
//                } else {
//                    //购买表数据修改(限定修改前状态）
//                    hsOrderPurchaseMapper.updateHsOrderPurchaseApplyDataForFail(orderPurchase.getId(),
//                            orderPurchase.getApply_shares(), orderPurchase.getApply_balance(),
//                            queryRequestPo.getDeduct_status(), TradeEnums.HSDeductStatusEnum.YFS.getValue(),
//                            TradeEnums.HSTradeStatusCharacterEnum.QRSB.getValue(),
//                            TradeEnums.HSTradeStatusCharacterEnum.DQR.getValue(),
//                            TradeEnums.HSTradeStatusEnum.QRSB.getValue());
//                    msg.setMsg("扣款失败");
//                    return msg;
//                }
//            } else {
//                msg.setMsg("交易结果查询为空");
//                return msg;
//            }
//        } else {
//            msg.setMsg("交易结果查询失败");
//            return msg;
//        }
//
//        return msg;
//    }

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

        //认购确认要考虑项目的状态 以及 订单的状态 ……

        HSOrderPurchase orderPurchase = hsOrderPurchaseMapper.findHsOrderPurchaseById(orderId);
        if (null == orderPurchase) {
            msg.setMsg("无法查询到订单");
            return msg;
        }
        HSProductInfo productInfo = hsProductMapper.findHSProductInfoById(orderPurchase.getHs_product_tid());

        //认购查询两次
        if (TradeEnums.HSTradeStatusEnum.DQR.getValue() != orderPurchase.getTrade_status_tid()
                && TradeEnums.HSTradeStatusEnum.XWQR.getValue() != orderPurchase.getTrade_status_tid()) {
            msg.setMsg("订单状态不正确");
            return msg;
        }

        String allot_no = orderPurchase.getAllot_no();
        String businflag = TradeEnums.HSBusinessApplyCodeEnum.SG.getValue(); //申购业务
        if (0 == orderPurchase.getTrade_type().intValue()) {
            businflag = TradeEnums.HSBusinessApplyCodeEnum.SG.getValue(); //申购业务
        } else if (1 == orderPurchase.getTrade_type().intValue()) {
            businflag = TradeEnums.HSBusinessApplyCodeEnum.RG.getValue(); //认购业务
        }
        int sort_way = 0;
        String trade_acco = orderPurchase.getTrade_acco();

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

                orderPurchase.setTrade_confirm_shares(queryResultPo.getTrade_confirm_shares());
                orderPurchase.setTrade_confirm_balance(queryResultPo.getTrade_confirm_balance());
                if (null != queryResultPo.getAffirm_date()) {
                    orderPurchase.setAffirm_date(queryResultPo.getAffirm_date());
                    orderPurchase.setConfirm_date(DateUtil.stringToDate(String.valueOf(queryResultPo.getAffirm_date()), "yyyyMMdd"));
                }
                orderPurchase.setTrade_status(queryResultPo.getTrade_status());

                Integer type = null;

                if (0 == orderPurchase.getTrade_type().intValue()
                        && TradeEnums.HSTradeStatusCharacterEnum.DQR.getValue().equals(orderPurchase.getTrade_status())) {
                    //申购
                    if (TradeEnums.HSTradeStatusCharacterEnum.QRCG.getValue().equals(queryResultPo.getTrade_status())) {
                        //确认成功
                        orderPurchase.setTrade_status_tid(TradeEnums.HSTradeStatusEnum.QRCG.getValue());
                        type = 10;
                    } else if (TradeEnums.HSTradeStatusCharacterEnum.BFQR.getValue().equals(queryResultPo.getTrade_status())) {
                        //部分确认
                        orderPurchase.setTrade_status_tid(TradeEnums.HSTradeStatusEnum.BFQR.getValue());
                        type = 10;
                    } else if (TradeEnums.HSTradeStatusCharacterEnum.QRSB.getValue().equals(queryResultPo.getTrade_status())) {
                        //确认失败
                        orderPurchase.setTrade_status_tid(TradeEnums.HSTradeStatusEnum.QRSB.getValue());
                        type = 1;
                    }
//                    else {
//                        //默认为失败
//                        orderPurchase.setTrade_status_tid(TradeEnums.HSTradeStatusEnum.QRSB.getValue());
//                        type = 1;
//                    }
                } else if (1 == orderPurchase.getTrade_type().intValue()
                        && TradeEnums.HSTradeStatusCharacterEnum.DQR.getValue().equals(orderPurchase.getTrade_status())
                        && TradeEnums.HSFundStatusEnum.ZC.getValue().equals(productInfo.getFund_status())) {
                    //认购（第一次确认）
                    if (TradeEnums.HSTradeStatusCharacterEnum.XWQR.getValue().equals(queryResultPo.getTrade_status())) {
                        orderPurchase.setTrade_status_tid(TradeEnums.HSTradeStatusEnum.XWQR.getValue());
                    } else if (TradeEnums.HSTradeStatusCharacterEnum.QRSB.getValue().equals(queryResultPo.getTrade_status())) {
                        orderPurchase.setTrade_status_tid(TradeEnums.HSTradeStatusEnum.XWQR.getValue());
                        type = 3;
                    }
//                    else {
//                        orderPurchase.setTrade_status_tid(TradeEnums.HSTradeStatusEnum.XWQR.getValue());
//                        type = 3;
//                    }
                } else if (1 == orderPurchase.getTrade_type().intValue()
                        && TradeEnums.HSTradeStatusCharacterEnum.XWQR.getValue().equals(orderPurchase.getTrade_status())
                        && TradeEnums.HSFundStatusEnum.FX.getValue().equals(productInfo.getFund_status())) {
                    //认购（第二次确认）
                    if (TradeEnums.HSTradeStatusCharacterEnum.QRCG.getValue().equals(queryResultPo.getTrade_status())) {
                        //确认成功
                        orderPurchase.setTrade_status_tid(TradeEnums.HSTradeStatusEnum.QRCG.getValue());
                        type = 11;
                    } else if (TradeEnums.HSTradeStatusCharacterEnum.BFQR.getValue().equals(queryResultPo.getTrade_status())) {
                        //部分确认
                        orderPurchase.setTrade_status_tid(TradeEnums.HSTradeStatusEnum.BFQR.getValue());
                        type = 11;
                    } else if (TradeEnums.HSTradeStatusCharacterEnum.QRSB.getValue().equals(queryResultPo.getTrade_status())) {
                        //确认失败
                        orderPurchase.setTrade_status_tid(TradeEnums.HSTradeStatusEnum.QRSB.getValue());
                        type = 3;
                    }
//                    else {
//                        //默认为失败
//                        orderPurchase.setTrade_status_tid(TradeEnums.HSTradeStatusEnum.QRSB.getValue());
//                        type = 3;
//                    }
                } else {
                    msg.setMsg("交易已不属于待处理状态");
                    return msg;
                }

                if (null != type) {
                    //基金购买表数据修改
                    hsOrderPurchaseMapper.updateHsOrderPurchaseTradeData(orderPurchase.getId(),
                            orderPurchase.getTrade_confirm_shares(), orderPurchase.getTrade_confirm_balance(),
                            orderPurchase.getTrade_status(), orderPurchase.getTrade_status_tid(),
                            orderPurchase.getAffirm_date(), orderPurchase.getConfirm_date(), orderPurchase.getFare_sx());

//                    msg = updateUserFundData(orderPurchase, type);
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
     * @param orderPurchase
     *          用户基金数据
     * @param type
     *          操作类型（0：申购中；1：申购失败；2：认购中；3：认购失败；10：申购成功；11：认购成功）
     * @return
     */
    private Msg updateUserFundData(HSOrderPurchase orderPurchase, int type) {
        Msg msg = new Msg();
        msg.setCode(Constant.FAIL);

        List<HSUserFundInfo> userFundList =  hsUserFundInfoMapper
                .findUserFundListByParams(orderPurchase.getUser_tid(), orderPurchase.getTrade_acco(),
                        orderPurchase.getHs_product_tid());

        if (userFundList.size() > 0) {
            HSUserFundInfo userFundInfo = userFundList.get(0);
            if (0 == type || 1 == type || 2 == type || 3 == type) { //购买中、购买失败
                hsUserFundInfoMapper.updateHSUserFundInfo1(orderPurchase.getApply_shares(),
                        orderPurchase.getApply_balance(), userFundInfo.getId(), type);
                msg.setCode(Constant.SUCCESS);
                msg.setMsg("用户基金表数据同步完成");
            } else if (10 == type || 11 == type) { //购买成功
                hsUserFundInfoMapper.updateHSUserFundInfo2(orderPurchase.getTrade_confirm_shares(),
                        orderPurchase.getApply_shares(), orderPurchase.getApply_balance(), userFundInfo.getId(), type);
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
     * 非空校验
     * @param req
     *          参数
     * @return
     */
    private Msg validateEmpty(PurchaseProductEvent req) {
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
        } else if (null == req.getBalance()) {
            msg.setMsg("购买金额不能为空");
        } else {
            msg.setCode(Constant.SUCCESS);
        }
        return msg;
    }


    /**
     * 认购
     * @param paramMap
     *          参数集合
     * @return
     */
    private SubscribeApply subscribeApply(Map<String, String> paramMap) {
        String url = appConfig.getURL_HS_SUBSCRIBEAPPLY();
        url = MessageFormat.format(url, appConfig.getIP_PORT());
        SubscribeApply apply = InterfaceCallingUtil.call(url, paramMap, InterfaceCallingUtil.POST, 0, SubscribeApply.class);
        return apply;
    }

    /**
     * 申购
     * @param paramMap
     *          参数集合
     * @return
     */
    private PurchaseApply purchaseApply(Map<String, String> paramMap) {
        String url = appConfig.getURL_HS_PURCHASEAPPLY();
        url = MessageFormat.format(url, appConfig.getIP_PORT());
        PurchaseApply apply = InterfaceCallingUtil.call(url, paramMap, InterfaceCallingUtil.POST, 0, PurchaseApply.class);
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
     * 计算手续费查询
     * @param paramMap
     *          参数集合
     * @return
     */
    private FeecalQueryApply feecalQueryApply(Map<String, String> paramMap) {
        String url = appConfig.getURL_HS_TRANSACTION_FEECAL_QUERY();
        url = MessageFormat.format(url, appConfig.getIP_PORT());
        FeecalQueryApply apply = InterfaceCallingUtil.call(url, paramMap, InterfaceCallingUtil.POST, 0,
                FeecalQueryApply.class);
        return apply;
    }


//    /**
//     * 批量查询并修改扣款结果
//     */
//    @Transactional
//    public void batchHandlerTradeApplyForPurchase() {
//        L.info("method batchHandlerTradeApplyForPurchase begin ...");
//        Map<String, Object> paramMap = new HashMap<>();
//        paramMap.put("trade_status_tid", String.valueOf(TradeEnums.HSTradeStatusEnum.DQR.getValue()));
//        paramMap.put("deduct_status", String.valueOf(TradeEnums.HSDeductStatusEnum.YFS.getValue()));
//        List<HSOrderPurchase> orderPurchaseList = hsOrderPurchaseMapper.findHsOrderPurchaseList(paramMap);
//        if (orderPurchaseList.size() > 0) {
//            orderPurchaseList.forEach(order -> handlerOrderPurchaseApply(order.getId()));
//        }
//        L.info("method batchHandlerTradeApplyForPurchase end ...");
//    }
    /**
     * 批量查询并修改交易结果
     */
    @Transactional
    public void batchHandlerTradeResultForPurchase() {
        L.info("method batchHandlerTradeResultForPurchase begin ...");
        Map<String, Object> paramMap = new HashMap<>();
        List<String> tradeStatusList = Arrays.asList(String.valueOf(TradeEnums.HSTradeStatusEnum.DQR.getValue()),
                String.valueOf(TradeEnums.HSTradeStatusEnum.XWQR.getValue()));
        paramMap.put("list", tradeStatusList);
        paramMap.put("deduct_status", String.valueOf(TradeEnums.HSDeductStatusEnum.YX.getValue()));
        List<HSOrderPurchase> orderPurchaseList = hsOrderPurchaseMapper.findHsOrderPurchaseList(paramMap);
        if (orderPurchaseList.size() > 0) {
            orderPurchaseList.forEach(order -> handlerTradeResultQuery(order.getId()));
        }
        L.info("method batchHandlerTradeResultForPurchase end ...");
    }

    public HSProductInfo findHSProductInfoById(int productId) {
        return  hsProductMapper.findHSProductInfoById(productId);
    }


    public Msg handleConfirmDebit(HSOrderPurchase orderPurchase){
            Msg msg=new Msg();
            //扣款成功
            //购买表数据修改(限定修改前状态）
            int purchaseUpdateResult = hsOrderPurchaseMapper.updateHsOrderPurchaseApplyData(orderPurchase.getId(),
                    orderPurchase.getApply_shares(), orderPurchase.getApply_balance(),
                    TradeEnums.HSDeductStatusEnum.YX.getValue(), TradeEnums.HSDeductStatusEnum.YFS.getValue(),
                    TradeEnums.HSTradeStatusCharacterEnum.DQR.getValue());
            if (purchaseUpdateResult > 0) {
                //用户基金表修改
                if (0 == orderPurchase.getTrade_type().intValue()) { //申购
                    msg = updateUserFundData(orderPurchase, 0);
                } else if (1 == orderPurchase.getTrade_type().intValue()) { //认购
                    msg = updateUserFundData(orderPurchase, 2);
                }
                if (Constant.FAIL == msg.getCode()) {
                    return msg;
                }
                msg.setCode(Constant.SUCCESS);
                msg.setData("基金购买申请已提交");
            } else {
                msg.setCode(Constant.SUCCESS);
                msg.setData("基金购买申请已经提交");
            }

            return  msg;
    }


    public Msg calcPurchaseRate(@RequestBody CalcPurchaseRateEvent event) throws ParseException, IllegalAccessException {
         Msg msg =new Msg();
        msg.setCode(Constant.FAIL);
        HSProductInfo productInfo = hsProductMapper.findHSProductInfoByFundCode(event.getFund_code());

        if(productInfo==null){
            msg.setMsg("该基金不存在");
            return  msg;
        }
        UserBankAccount bankAccount = userBankAccountMapper.findUserBankAccountById(event.getBankAccounId());
        if (null == bankAccount) {
            msg.setMsg("查询不到银行账号");
            return msg;
        }
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("fund_code", event.getFund_code()); //基金代码
        if (TradeEnums.HSFundStatusEnum.FX.getValue().equals(productInfo.getFund_status())) {  //业务类型
            paramMap.put("fund_busin_code", TradeEnums.HSBusinessApplyCodeEnum.RG.getValue());  //认购
        }else if(TradeEnums.HSFundStatusEnum.ZC.getValue().equals(productInfo.getFund_status())){
            paramMap.put("fund_busin_code", TradeEnums.HSBusinessApplyCodeEnum.SG.getValue());  //申购
        }else {
            msg.setMsg("该基金状态不支持购买");
            return msg;
        }
        paramMap.put("trade_acco", bankAccount.getTrade_acco()); //交易帐号
        paramMap.put("share_type",productInfo.getShare_type().toString());  //收费类型
        paramMap.put("apply_sum",event.getBalance().toString());//金额

        FeecalQueryApply  apply = feecalQueryApply(paramMap);
        if(!apply.getCode().equals(Constant.HS_SUCCESS_CODE)){
            msg.setMsg("查询失败");
            return msg;
        }

        Map fl=(Map) PHPSerializer.unserialize(productInfo.getFl().getBytes());
        CalcRatePojo ratePojo =new CalcRatePojo();
        NumberFormat nf  =  NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits( 2 );
        ratePojo.setFare_sx(apply.getFare_sx());
        ratePojo.setRate(nf.format(apply.getRate()));
        ratePojo.setConfirm_date((String)fl.get("sqsj"));
        msg.setData(ratePojo);
        msg.setCode(Constant.SUCCESS);
        msg.setMsg("查询成功");
        return  msg;



    }

}
