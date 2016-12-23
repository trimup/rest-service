package com.lihe.service;

import com.lihe.AppConfig;
import com.lihe.HundSunReply.TradeResultQueryApply;
import com.lihe.HundSunReply.TradeResultQueryPo;
import com.lihe.common.Constant;
import com.lihe.common.Msg;
import com.lihe.common.TradeEnums;
import com.lihe.entity.BankAccountInfo;
import com.lihe.entity.HSBankCodeInfo;
import com.lihe.entity.UserInfo;
import com.lihe.event.userBank.UserBankData;
import com.lihe.persistence.HSBankMapper;
import com.lihe.persistence.HSUserFundInfoMapper;
import com.lihe.persistence.UserBankAccountMapper;
import com.lihe.persistence.UserInfoMapper;
import com.lihe.pojo.BankCodePojo;
import com.lihe.pojo.UserBankPojo;
import com.lihe.until.InterfaceCallingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by trimup on 2016/8/19.
 */
@Service
public class UserBankService {


    private static final Logger L   =  LoggerFactory.getLogger(UserBankService.class);



    @Autowired
    private UserBankAccountMapper userBankAccountMapper;
    @Autowired
    private HSBankMapper hsBankMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;



    @Autowired
    private AppConfig appConfig;

    /**
     * 获取用户银行信息
     * @param user_tid
     * @return
     */
    public Msg getUserBankInfo(Integer user_tid){
        Msg msg =new Msg();
        UserBankData data =new UserBankData();
        List<BankAccountInfo>  userBankList =userBankAccountMapper.queryAccountByUserId(user_tid);
        List<UserBankPojo> ubList=new ArrayList<>();
        for(BankAccountInfo b : userBankList){
            UserBankPojo p =new UserBankPojo();
            p.setId(b.getId());  //id
            p.setBank_code(b.getBank_no()); //银行编号
            p.setBank_name(b.getBank_name());//银行名称
            p.setBank_account(b.getBank_account());//银行卡号
            HSBankCodeInfo hsBankCodeInfo =hsBankMapper.queryHsBankCode(b.getBank_no());
            if(hsBankCodeInfo!=null){
                p.setBank_day_limit(hsBankCodeInfo.getDay_limit());
                p.setBank_per_limit(hsBankCodeInfo.getOne_limit());
            }
            ubList.add(p);
        }
        data.setList(ubList);
        msg.setCode(Constant.SUCCESS);
        msg.setData(data);
        msg.setMsg(Constant.SUCCESS_MSG);
        return  msg;
    }


    /**
     * 获取银行编码信息
     */
    public List<BankCodePojo> getBankCodeInfo(){
        List<HSBankCodeInfo> bankCodeInfoList =hsBankMapper.getAllBankCode();
        List<BankCodePojo> ubList=new ArrayList<>();
        for(HSBankCodeInfo h : bankCodeInfoList){
            BankCodePojo b =new BankCodePojo();
            b.setHs_code(h.getHs_code());
            b.setBank_day_limit(h.getDay_limit());
            b.setBank_name(h.getBank_name());
            b.setBank_per_limit(h.getOne_limit());
            ubList.add(b);
        }
        return  ubList;
    }

    /**
     * 获取用户银行信息
     * @param user_tid
     * @return
     */
    public Msg getUserBankInfoForRedeem(Integer user_tid, String fund_code){
        Msg msg =new Msg();
        UserBankData data =new UserBankData();
        List<UserBankPojo> ubList=new ArrayList<>();
        UserInfo   userInfo =userInfoMapper.findUserById(user_tid);
        //可赎回的基金
        // 调用恒生接口查询 ----> 交易申请查询
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("sort_way", String.valueOf(1)); //倒序查询
        paramMap.put("client_id", userInfo.getClient_id());
        //交易结果查询
        TradeResultQueryApply result = tradeResultQueryApply(paramMap);
        if (null == result) {
            msg.setCode(Constant.FAIL);
            msg.setMsg("查询错误");
            return msg;
        }
        List<TradeResultQueryPo> tradeResultQueryPos= Arrays.asList(result.getTradeResultQuerys());
        if(tradeResultQueryPos==null||tradeResultQueryPos.isEmpty()){
            msg.setCode(Constant.FAIL);
            msg.setMsg("查询不到该用户银行卡信息");
            return msg;
        }
        tradeResultQueryPos =tradeResultQueryPos.stream().
                filter(o->o.getFund_code().equals(fund_code)&&
                        (o.getFund_busin_code().equals(TradeEnums.HSBusinessApplyCodeEnum.SG.getValue())||
                                o.getFund_busin_code().equals(TradeEnums.HSBusinessApplyCodeEnum.SG.getValue()))&&
                        (o.getTrade_status().equals(TradeEnums.HSTradeStatusCharacterEnum.QRCG.getValue())||
                                o.getTrade_status().equals(TradeEnums.HSTradeStatusCharacterEnum.BFQR.getValue()))
                ).collect(Collectors.toList());
        if(tradeResultQueryPos==null||tradeResultQueryPos.isEmpty()){
            msg.setCode(Constant.FAIL);
            msg.setMsg("查询不到该用户银行卡信息");
            return msg;
        }

        if (tradeResultQueryPos.size() > 0) {
            //交易账户集合
            List<String> tradeAccoList = tradeResultQueryPos.stream()
                    .map(TradeResultQueryPo::getTrade_acco)
                    .collect(Collectors.toList());
            //匹配
            List<BankAccountInfo>  userBankList =userBankAccountMapper.queryAccountByUserId(user_tid);
            for(BankAccountInfo b : userBankList){
                //筛选
                if (!tradeAccoList.contains(b.getTrade_acco())) {
                    continue;
                }
                UserBankPojo p =new UserBankPojo();
                p.setId(b.getId());  //id
                p.setBank_code(b.getBank_no()); //银行编号
                p.setBank_name(b.getBank_name());//银行名称
                p.setBank_account(b.getBank_account());//银行卡号
                HSBankCodeInfo hsBankCodeInfo =hsBankMapper.queryHsBankCode(b.getBank_no());
                if(hsBankCodeInfo!=null){
                    p.setBank_day_limit(hsBankCodeInfo.getDay_limit());
                    p.setBank_per_limit(hsBankCodeInfo.getOne_limit());
                }
                ubList.add(p);
            }
        }
        data.setList(ubList);
        msg.setCode(Constant.SUCCESS);
        msg.setData(data);
        msg.setMsg(Constant.SUCCESS_MSG);
        return  msg;
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

}
