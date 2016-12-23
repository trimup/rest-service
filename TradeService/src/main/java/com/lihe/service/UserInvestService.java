package com.lihe.service;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lihe.AppConfig;
import com.lihe.common.Constant;
import com.lihe.common.Msg;
import com.lihe.common.TradeEnums;
import com.lihe.entity.*;
import com.lihe.event.hsOrder.HSOrderListQueryEvent;
import com.lihe.event.hsOrder.QueryHSOrderListEvent;
import com.lihe.event.userInvest.*;
import com.lihe.persistence.*;
import com.lihe.pojo.*;
import com.lihe.replay.hsTrade.*;
import com.lihe.replay.userInvest.FloatingProfitReply;
import com.lihe.replay.userInvest.ModifyBonusReply;
import com.lihe.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by trimup on 2016/9/5.
 * 用户投资
 */
@Service
public class UserInvestService {
    private static final Logger L = LoggerFactory.getLogger(UserInvestService.class);
    private static final SimpleDateFormat Format = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat ORDER_Format = new SimpleDateFormat("yyyyMMddHHmmssSS");

    @Autowired
    private HSUserFundInfoMapper hsUserFundInfoMapper;
    @Autowired
    private HSProductMapper hsProductMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private FundIncomeMapper fundIncomeMapper;
    @Autowired
    private HSOrderPurchaseMapper hsOrderPurchaseMapper;
    @Autowired
    private UserFundShareMapper userFundShareMapper;

    @Autowired
    private AppConfig appconfig;


    /**
     * 获取用户投资的公募基金列表
     *
     * 本地交易状态
     * 1.付款为验证 （支付失败）
     * 2.支付失败
     * 3.支付中
     * 4.申请中中
     * 5.基金公司确认失败
     * 6.已持仓
     * 7.认购行为确认成功
     * 8.赎回中
     * 9.赎回失败
     * 10.赎回成功
     *
     *
     * @param
     */
    public Msg  getUserInvestList(QueryUserInvestEvent event) throws ParseException {
        //查询出该用户信息
        Msg msg =new Msg();
        UserInfo  userInfo =userInfoMapper.findUserById(event.getUser_tid());
        //调用恒生接口查询 ---->  交易结果查询
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("sort_way", String.valueOf(1)); //倒序查询
        paramMap.put("client_id", userInfo.getClient_id());
        //交易结果查询
        TradeResultQueryApply result = tradeResultQueryApply(paramMap);
        if (null == result) {
            msg.setCode(Constant.FAIL);
            msg.setMsg("交易结果查询失败");
            return msg;
        }
        List<TradeResultQueryPo> tradeResultQueryPos=Arrays.asList(result.getTradeResultQuerys());
        List<UserInvestListPojo>  userInvestListPojos =new ArrayList<>();
        for(TradeResultQueryPo t :tradeResultQueryPos) {
            UserInvestListPojo u = new UserInvestListPojo();
            //查询 该基金信息
            HSProductInfo hsProductInfo =
                    hsProductMapper.findHSProductInfoByFundCode(t.getFund_code());
            u.setFund_code(t.getFund_code());
            u.setFund_name(hsProductInfo.getFund_name());
            u.setAllot_no(t.getAllot_no());
            u.setOrder_date(ORDER_Format.parse(t.getOrder_date_time()+"0"));
            //如果是申购业务
            if (t.getFund_busin_code().equals(TradeEnums.HSBusinessApplyCodeEnum.SG.getValue())) {
                //转换申购交易记录
                handlePurchaseTran(t, u, hsProductInfo);
                } else if (t.getFund_busin_code().equals(TradeEnums.HSBusinessApplyCodeEnum.RG.getValue())) {  //如果是认购业务
                //转换认购交易记录
                handleSubsTran(t, u, hsProductInfo);
                } else if (t.getFund_busin_code().equals(TradeEnums.HSBusinessApplyCodeEnum.SH.getValue())) {
                //转换赎回交易记录
                handleRedeeTran(userInvestListPojos, t, u, hsProductInfo);
                }
              userInvestListPojos.add(u);
        }
        List<MyInvestListPojo> myInvestLists=new ArrayList<>();
        //剔除掉非当天失败的交易
        userInvestListPojos=userInvestListPojos.stream().filter(o->!((
                o.getSort_type()==1||o.getSort_type()==2||o.getSort_type()==5||o.getSort_type()==9)
                &&DateUtil.countDiffDay(o.getOrder_date(),new Date())!=0)).collect(Collectors.toList());
        //申请中和失败的交易（包括  扣款未校验，支付失败 支付中 等待确认中 确认失败 赎回中 赎回失败的 的交易 ）按时间排序
        List<UserInvestListPojo> userInvestOngoingAndFailList =
                userInvestListPojos.stream().filter(o -> o.getSort_type()==1||o.getSort_type()==2||
                        o.getSort_type()==3||o.getSort_type()==4||o.getSort_type()==5||o.getSort_type()==8||o.getSort_type()==9).
                        sorted((o1, o2) -> o2.getOrder_date().compareTo(o1.getOrder_date())).collect(Collectors.toList());//申请中的交易
        //放入列表中
        for(UserInvestListPojo u :userInvestOngoingAndFailList){
            MyInvestListPojo m =new MyInvestListPojo();
            m.setFund_code(u.getFund_code());
            m.setFund_name(u.getFund_name());
            m.setHave_money(u.getHave_money());
            m.setBusiness_share(u.getBusiness_share());
            m.setSort_type(u.getSort_type());
            m.setStatus(u.getStatus());
            m.setAllot_no(u.getAllot_no());
            myInvestLists.add(m);
        }

        //-------------------------认购行为确认的交易
        //如果用户拥有同基金认购的交易 合并
        mergeTransaction(myInvestLists, userInvestListPojos,7,"认购行为确认成功");
        //如果用户拥有同基金的已持仓的交易 合并  （）已持仓的数据 从份额查询接口拉取）
//        mergeTransaction(MyInvestLists, userInvestListPojos,6,"已持仓");
        //如果用户拥有同基金的已赎回的交易 合并  （目前不显示已赎回的基金）
//        mergeTransaction(MyInvestLists, userInvestListPojos,10);
        //已持仓的基金
        Map<String, String> shareQueryParamMap = new HashMap<>();
        shareQueryParamMap.put("client_id", userInfo.getClient_id() );
        shareQueryParamMap.put("isFilter", "1");
        ShareQueryReply reply =shareQueryApply(shareQueryParamMap);
        if(reply.getCode().equals(Constant.HS_SUCCESS_CODE)){
            if(reply.getShareQuerys()!=null&&!reply.getShareQuerys().isEmpty()){
                List<ShareQueryPo> shareQuerys =reply.getShareQuerys();
                for(ShareQueryPo s : shareQuerys){
                    MyInvestListPojo m =new MyInvestListPojo();
                    m.setBusiness_share(s.getCurrent_share());
                    m.setFund_code(s.getFund_code());
                    m.setFund_name(s.getFund_name());
                    m.setHave_money(s.getWorth_value());
                    m.setSort_type(6);
                    m.setStatus("已持仓");
                    myInvestLists.add(m);
                }
            }
        }

        UserInvestData data  =new UserInvestData();
        //筛选
        if(event.getSort_type()!=0){
            myInvestLists=myInvestLists.stream().filter(
                    o->o.getSort_type()==event.getSort_type()).collect(Collectors.toList());
        }
        List<MyInvestListPojo> pageList = new ArrayList<>();
        for (int i = (event.getPage() - 1) * event.getPageSize();
             i < (event.getPage() * event.getPageSize() > myInvestLists.size() ?
                     myInvestLists.size() : event.getPage() * event.getPageSize()); ++i){
            pageList.add(myInvestLists.get(i));
        }
        data.setTotal(myInvestLists.size());
        data.setPage(event.getPage());
        data.setPageSize(event.getPageSize());
         data.setTotalPage(myInvestLists.isEmpty()?0:NumberUtil.Division(userInvestListPojos.size(),event.getPageSize()));


        data.setList(pageList);
        msg.setCode(Constant.SUCCESS);
        msg.setMsg(Constant.SUCCESS_MSG);
        msg.setData(data);
        return msg;

    }

    /**
     * 转换赎回交易记录
     * @param userInvestListPojos       转换记录存放容器
     * @param t                   产寻恒生交易记录
     * @param u                   本地交易记录
     * @param hsProductInfo             基金详情
     * @throws ParseException
     */
    private void handleRedeeTran(List<UserInvestListPojo> userInvestListPojos,
                                 TradeResultQueryPo t, UserInvestListPojo u, HSProductInfo hsProductInfo) throws ParseException {
        //如果是赎回业务
        if (t.getTrade_status().equals(
                TradeEnums.HSTradeStatusCharacterEnum.DQR.getValue())) { //交易状态 待确认
            u.setStatus("赎回中");
            u.setSort_type(8);
            u.setBusiness_share(t.getApply_share());
        } else if (t.getTrade_status().equals(
                TradeEnums.HSTradeStatusCharacterEnum.QRSB.getValue())) { //确认失败
            u.setStatus("赎回失败");
            u.setSort_type(9);
            u.setBusiness_share(t.getApply_share());
        } else if (t.getTrade_status().equals(
                TradeEnums.HSTradeStatusCharacterEnum.QRCG.getValue())) {//确认成功
            u.setStatus("赎回成功");
            u.setSort_type(10);
            u.setBusiness_share(t.getTrade_confirm_shares());
        } else if (t.getTrade_status().equals(
                TradeEnums.HSTradeStatusCharacterEnum.BFQR.getValue())) {//部分确认
            //如果是部分确认的
            // 确认成功部分为一条记录
            u.setBusiness_share(t.getTrade_confirm_shares());
            u.setSort_type(10);
            u.setStatus("赎回成功");
            //赎回中的为一条记录
            UserInvestListPojo redeU = new UserInvestListPojo();
            redeU.setFund_code(t.getFund_code());
            redeU.setFund_name(hsProductInfo.getFund_name());
            redeU.setOrder_date(ORDER_Format.parse(t.getOrder_date_time()+"0"));
            redeU.setBusiness_share(t.getApply_share().subtract(t.getTrade_confirm_shares()));
            redeU.setSort_type(8);
            redeU.setStatus("赎回中");
            userInvestListPojos.add(redeU);
        }
    }

    /**
     * 转换认购交易记录
     * @param t                      产寻恒生交易记录
     * @param u                      本地交易记录
     * @param hsProductInfo                基金详情
     */
    private void handleSubsTran(TradeResultQueryPo t, UserInvestListPojo u, HSProductInfo hsProductInfo) {
        if (t.getDeduct_status().equals(TradeEnums.HSDeductStatusEnum.WJY.getValue())) {  //扣款指令未校验
            u.setStatus("扣款未校验");
            u.setSort_type(1);
            u.setHave_money(t.getBalance());
        } else if (t.getDeduct_status().equals(TradeEnums.HSDeductStatusEnum.WX.getValue())) { //扣款指令发送失败
            u.setStatus("支付失败");
            u.setSort_type(2);
            u.setHave_money(t.getBalance());
        } else if (t.getDeduct_status().equals(TradeEnums.HSDeductStatusEnum.YFS.getValue())) { //扣款指令发送中
            u.setStatus("支付中");
            u.setHave_money(t.getBalance());
            u.setSort_type(3);
        } else if (t.getDeduct_status().equals(TradeEnums.HSDeductStatusEnum.YX.getValue())) {  //扣款指令发送成功
            if (t.getTrade_status().equals(
                    TradeEnums.HSTradeStatusCharacterEnum.DQR.getValue())) { //交易状态 待确认
                u.setStatus("申请中");
                u.setSort_type(4);
                u.setHave_money(t.getBalance());
            } else if (t.getTrade_status().equals(
                    TradeEnums.HSTradeStatusCharacterEnum.QRSB.getValue())) { //确认失败
                u.setStatus("基金公司确认失败");
                u.setSort_type(5);
                u.setHave_money(t.getBalance());
            } else if (t.getTrade_status().equals(
                    TradeEnums.HSTradeStatusCharacterEnum.QRCG.getValue())) {//确认成功
                u.setStatus("已持仓");
                u.setSort_type(6);
                u.setHave_money(t.getTrade_confirm_shares().multiply(hsProductInfo.getNet_value()));
                u.setBusiness_share(t.getTrade_confirm_shares());
            } else if (t.getTrade_status().equals(
                    TradeEnums.HSTradeStatusCharacterEnum.BFQR.getValue())) {//部分确认
                u.setStatus("部分确认成功");
                u.setSort_type(6);
                u.setHave_money(t.getTrade_confirm_shares().multiply(hsProductInfo.getNet_value()));
                u.setBusiness_share(t.getTrade_confirm_shares());
            } else if (t.getTrade_status().equals(
                    TradeEnums.HSTradeStatusCharacterEnum.XWQR.getValue())) {//行为确认
                u.setStatus("认购行为确认成功");
                u.setSort_type(7);
                u.setHave_money(t.getBalance());
            }
        }
    }


    /**
     * 处理申购交易
     * @param t    交易查询结果
     * @param u     本地交易记录
     * @param hsProductInfo   基金信息
     */
    private void handlePurchaseTran(TradeResultQueryPo t, UserInvestListPojo u, HSProductInfo hsProductInfo) {
        if (t.getDeduct_status().equals(TradeEnums.HSDeductStatusEnum.WJY.getValue())) {  //扣款指令未校验
            u.setStatus("扣款未校验");
            u.setSort_type(1);
            u.setHave_money(t.getBalance());
        } else if (t.getDeduct_status().equals(TradeEnums.HSDeductStatusEnum.WX.getValue())) { //扣款指令发送失败
            u.setStatus("支付失败");
            u.setSort_type(2);
            u.setHave_money(t.getBalance());
        } else if (t.getDeduct_status().equals(TradeEnums.HSDeductStatusEnum.YFS.getValue())) { //扣款指令发送中
            u.setStatus("支付中");
            u.setHave_money(t.getBalance());
            u.setSort_type(3);
        } else if (t.getDeduct_status().equals(TradeEnums.HSDeductStatusEnum.YX.getValue())) {  //扣款指令发送成功
            if (t.getTrade_status().equals(
                    TradeEnums.HSTradeStatusCharacterEnum.DQR.getValue())) { //交易状态 待确认
                u.setStatus("申请中");
                u.setSort_type(4);
                u.setHave_money(t.getBalance());
            } else if (t.getTrade_status().equals(
                    TradeEnums.HSTradeStatusCharacterEnum.QRSB.getValue())) { //确认失败
                u.setStatus("基金公司确认失败");
                u.setSort_type(5);
                u.setHave_money(t.getBalance());
            } else if (t.getTrade_status().equals(
                    TradeEnums.HSTradeStatusCharacterEnum.QRCG.getValue())) {//确认成功
                u.setStatus("已持仓");
                u.setHave_money(t.getTrade_confirm_shares().multiply(hsProductInfo.getNet_value()));
                u.setBusiness_share(t.getTrade_confirm_shares());
                u.setSort_type(6);
            }
        }
    }

    /**
     * 合并   用户某种类型的交易
     * @param myInvestLists       投资列表
     * @param userInvestListPojos   用户从恒生获取到的交易列表
     * @param sort_type       交易类型
     */
    private void mergeTransaction(List<MyInvestListPojo> myInvestLists, List<UserInvestListPojo> userInvestListPojos,Integer sort_type,String trade_status) {
        List<UserInvestListPojo> filterList =
                userInvestListPojos.stream().filter(o -> o.getSort_type() == sort_type).collect(Collectors.toList());//认购行为确认的交易
        if (!filterList.isEmpty()) {
            //该用户认购的基金代码
            List<String> holdFundCodes =
                    filterList.stream().map(
                            o -> o.getFund_code()).distinct().collect(Collectors.toList());
            for (String fund_code : holdFundCodes) {
                //获取该基金的持仓的所有记录
                List<UserInvestListPojo> userInvestFundCode =
                        filterList.stream().filter
                                (o -> o.getFund_code().equals(fund_code)).collect(Collectors.toList());
                MyInvestListPojo m = new MyInvestListPojo();
                m.setFund_code(fund_code);
                m.setSort_type(sort_type);
                m.setStatus(trade_status);
                for (UserInvestListPojo u : userInvestFundCode) {
                    m.setFund_name(u.getFund_name());
                    m.setHave_money(m.getHave_money().add(u.getHave_money()));
                    m.setBusiness_share(m.getBusiness_share().add(u.getBusiness_share()));
                }
                myInvestLists.add(m);
            }
        }
    }


    /**
     * 获取我的账户信息
     * @param user_tid
     */
    public  UserAccountPojo  getMyAccount(Integer user_tid){

        Msg msg =new Msg();
        UserInfo  userInfo =userInfoMapper.findUserById(user_tid);
        //调用恒生接口查询 ---->  交易结果查询
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("sort_way", String.valueOf(1)); //倒序查询
        paramMap.put("client_id", userInfo.getClient_id());
        //交易结果查询
        TradeResultQueryApply result = tradeResultQueryApply(paramMap);
        BigDecimal total_money =new BigDecimal(0);
        String  date ;
        //用户申请中的金额
        if(result.getCode().equals(Constant.HS_SUCCESS_CODE) &&result.getTradeResultQuerys()!=null){
            //获取申请中的交易申请
            List<TradeResultQueryPo> tradeResultQueryPos=Arrays.asList(result.getTradeResultQuerys());
            tradeResultQueryPos =tradeResultQueryPos.stream().
                    filter(o->
                            (o.getFund_busin_code().equals(TradeEnums.HSBusinessApplyCodeEnum.RG.getValue())||o.getFund_busin_code().equals(TradeEnums.HSBusinessApplyCodeEnum.SG.getValue()))&&
                                    (o.getDeduct_status().equals(TradeEnums.HSDeductStatusEnum.YX.getValue()))&&
                                    (o.getTrade_status().equals(TradeEnums.HSTradeStatusCharacterEnum.XWQR.getValue())||
                                            o.getTrade_status().equals(TradeEnums.HSTradeStatusCharacterEnum.DQR.getValue())
                                    )
                    ).collect(Collectors.toList());
            if(tradeResultQueryPos!=null&&!tradeResultQueryPos.isEmpty()){
                for(TradeResultQueryPo t :tradeResultQueryPos){
                    total_money=total_money.add(t.getBalance());
                }
            }

        }

        //已持仓的金额
        Map<String, String> shareQueryParamMap = new HashMap<>();
        shareQueryParamMap.put("client_id", userInfo.getClient_id() );
        ShareQueryReply reply =shareQueryApply(shareQueryParamMap);
        if(reply.getCode().equals(Constant.HS_SUCCESS_CODE)){
            if(reply.getShareQuerys()!=null&&!reply.getShareQuerys().isEmpty()){
                List<ShareQueryPo> shareQuerys =reply.getShareQuerys();
                for(ShareQueryPo s : shareQuerys){
                    total_money=total_money.add(s.getWorth_value()); //
                }
            }
        }
        String yesterdayDate=Format.format(DateUtil.getDayBefore(new Date()));
        UserAccountPojo   accountPojo =new UserAccountPojo();
        //查询用户昨日收益
        UserIncomeInfo userIncomeInfo =fundIncomeMapper.queryUserIncomeByDate(user_tid,yesterdayDate);
        //查询用户累计收益
        BigDecimal cumulative_profit_loss =fundIncomeMapper.getCumulativeProfit(user_tid); //累计盈亏
        //当日收益
        if(userIncomeInfo==null){
            accountPojo.setToday_profit_loss(userIncomeInfo.getIncome().toString());
        }else{
            accountPojo.setToday_profit_loss("0");
        }
        accountPojo.setTotal_money(total_money); //账户总资产
        accountPojo.setCumulative_profit_loss(cumulative_profit_loss.toString());
        //最近的收益日
        accountPojo.setDate(yesterdayDate);
        return  accountPojo;

    }


    /**
     * 统计用户每天的收益
     */
    public void statUserIncomeEveryDay(){
        //查询有购买过公募基金的交易帐号
        List<Integer> haveBuyTradeAcco=hsOrderPurchaseMapper.queryPurchaseUserTradeAcco();
        //将所有用户持有的基金数据落地
        haveBuyTradeAcco.forEach(o->statsEveryUser(o));
        String yesterdayDate=Format.format(DateUtil.getDayBefore(new Date()));
        //获取昨天持有基金的用户 的基金记录
        List<UserFundShareInfo> haveShareYesterday=userFundShareMapper.queryUserShareYesterday(yesterdayDate);
        //昨日持有基金的用户
        List<Integer> haveShareYesterdayUser=
                haveShareYesterday.stream().map(UserFundShareInfo::getUser_tid).distinct().collect(Collectors.toList());
        for(Integer h : haveShareYesterdayUser){
            //当前用户拥有的昨日持有基金记录
            List<UserFundShareInfo> currentUserHaveShare=
                    haveShareYesterday.stream().filter(o->o.getUser_tid()==h).collect(Collectors.toList());
            BigDecimal  today_income=new BigDecimal(0);
            //遍历当前用户昨日持有的所有的基金
            for(UserFundShareInfo yu : currentUserHaveShare){
                //查询当前基金今天是否仍然持有
                String todayDate=Format.format(new Date());
                UserFundShareInfo tu=userFundShareMapper.queryUserShareByYesterdayRecord(todayDate,yu.getUser_tid(),yu.getFund_code());
                if(tu!=null){//如果今日仍然持有  --》计算该基金的昨日收益
                    //昨日的收益           今日净值*今日份额-昨日净值*昨日份额-今日买入/转入确认金额+今日卖出/转出确认金额+今日强制赎回确认金额
                    BigDecimal fund_income =tu.getNet_value().multiply(tu.getCurrent_share()).  //今日市值
                            subtract(yu.getNet_value().multiply(yu.getCurrent_share())). //-昨日市值
                            subtract(tu.getToday_apply_total_quota()).  //-今日买入
                            subtract(tu.getToday_transin_total_quota()).//-今日转入
                            add(tu.getToday_exceed_total_quota()). //+今日卖出
                            add(tu.getToday_transout_total_quota()); //+今日转出
                    today_income=today_income.add(fund_income);
                    FundIncomeInfo yesterdayFundIncome = new FundIncomeInfo();
                    yesterdayFundIncome.setFund_code(yu.getFund_code());
                    yesterdayFundIncome.setIncome(fund_income);
                    yesterdayFundIncome.setIncome_date(yesterdayDate);
                    yesterdayFundIncome.setUser_tid(h);
                    //插入用户该基金收益
                    fundIncomeMapper.insertFundIncome(yesterdayFundIncome);
                    //插入用户该基金的收益进入历史表
                    fundIncomeMapper.insertFundIncomeHistory(yesterdayFundIncome);
                }else{  //如果当前基金 今天用户已经未持有了
                    //删除掉用户持有的该基金的份额信息
                    userFundShareMapper.deleteUserFundShare(h,yu.getFund_code());
                    //删除掉用户对于该基金的收益
                    fundIncomeMapper.delectFundIncomeOnUnhold(yu.getFund_code(),h);
                }

            }
            //如果用户当天的收益不为0 插入到用户收益表中
            if(today_income.compareTo(new BigDecimal(0))!=0){
                //用户的昨日收益
                 fundIncomeMapper.insertUserEveryIncome(h,today_income,yesterdayDate);
            }
        }
        }


    /**
     * 处理用户的每日收益
     * @param
     * @return
     */
    public void statsEveryUser(Integer user_tid) {

        UserInfo   userInfo= userInfoMapper.findUserById(user_tid);
        //落地每日的 用户份额
        Map<String, String> shareQueryParamMap = new HashMap<>();
        shareQueryParamMap.put("client_id", userInfo.getClient_id() );
        ShareQueryReply reply =shareQueryApply(shareQueryParamMap);
        if(reply.getCode().equals(Constant.HS_SUCCESS_CODE)){
            if(reply.getShareQuerys()!=null&&!reply.getShareQuerys().isEmpty()){
                List<ShareQueryPo> shareQuerys =reply.getShareQuerys();
                for(ShareQueryPo s : shareQuerys){
                    userFundShareMapper.insertUserFundShare(s,user_tid);
                    userFundShareMapper.insertUserFundShareHistroy(s,user_tid);
                }
            }
        }


    }


    /**
     * 查询用户浮动收益
     * @param paramMap
     * @return
     */
    private FloatingProfitReply queryUserFloatProfit(Map<String, String> paramMap) {
        String url =appconfig.getURL_HS_FLOATING_PROFIT_QUERY() ;
        url = MessageFormat.format(url, appconfig.getIP_PORT());
        FloatingProfitReply reply = InterfaceCallingUtil.call(
                url, paramMap, InterfaceCallingUtil.POST, 0, FloatingProfitReply.class);
        return reply;
    }

    /**
     * 掉用恒生接口修改用户分红方式
     * @param paramMap
     * @return
     */
    private ModifyBonusReply hsModifybonus(Map<String, String> paramMap) {
        String url = appconfig.getURL_HS_MODIFY_BONUS();
        url = MessageFormat.format(url, appconfig.getIP_PORT());
        ModifyBonusReply reply = InterfaceCallingUtil.call(
                url, paramMap, InterfaceCallingUtil.POST, 0, ModifyBonusReply.class);
        return reply;
    }



    /**
     * 修改用户分红方式
     */
    public Msg   modifyUserFundBonus(ModifyBonusEvent event) throws Exception{
        Msg msg= new Msg();
        HSProductInfo hsProductInfo =hsProductMapper.queryHSProductByFundCode(event.getFund_code());
        UserInfo userInfo =userInfoMapper.findUserById(event.getUser_tid());
        //查询出该用户投资的基金详情
        List<HSUserFundInfo>  userFundInfos =hsUserFundInfoMapper.
                queryUserFundByCode(event.getUser_tid(),event.getFund_code());
        //过滤掉用户已经未持有的基金
        userFundInfos =userFundInfos.stream().filter(
                p-> !(p.getFrozen_shares().intValue()==0
                        &&p.getEnable_shares().intValue()==0
                        &&p.getPurchased_shares().intValue()==0)).collect(Collectors.toList());
        //然后修改该用户的分红方式
        //如果该用户并未持有该基金 返回错误
        if(userFundInfos==null||userFundInfos.isEmpty()){
            L.info("user_tid"+event.getUser_tid()+" fix fund_code "+event.getFund_code()+"fail ");
            return  new Msg(Constant.FAIL,"修改失败");
        }else {
            //如果该用户持有该基金 修改分红方式
            for(HSUserFundInfo h:userFundInfos){
                Map<String, String> paramMap = new HashMap<>();
                paramMap.put("auto_buy", event.getAuto_buy());
                paramMap.put("fund_code", event.getFund_code());
                paramMap.put("password", DESUntil.decode(appconfig.getDES_KEY(),
                        com.lihe.util.Base64.decode(userInfo.getTrade_password())));
                paramMap.put("share_type", hsProductInfo.getShare_type().toString());
                paramMap.put("trade_acco", h.getTrade_acco());
                ModifyBonusReply reply= hsModifybonus(paramMap);
                L.info("user_tid"+event.getUser_tid()+" fix fund_code "+
                        event.getFund_code()+"result is  "+reply.toString());
                if(reply.getCode().equals(Constant.HS_SUCCESS_CODE)){
                    //修改分红方式
                    hsUserFundInfoMapper.
                            updateUserFundAutoType(event.getAuto_buy(),h.getId());
                }else {
                    msg.setCode(Constant.FAIL);
                    msg.setMsg(reply.getMessage());
                    return  msg;
                }
            }
        }
        msg.setCode(Constant.SUCCESS);
        msg.setMsg(Constant.SUCCESS_MSG);
        return  msg;
    }



    public UserIncomeData queryUserIncome(QueryUserIncomeEvent event){
        UserIncomeData data =new UserIncomeData();
        PageHelper.startPage(event.getPage(),event.getPageSize());
        List<UserIncomeInfo> userIncomeInfos =fundIncomeMapper.
                queryUserIncome(event.getUser_tid());
        List<UserIncomePojo> userIncomePojos =new ArrayList<>();
        for(UserIncomeInfo u : userIncomeInfos){
            UserIncomePojo p =new UserIncomePojo();
            p.setDate(u.getIncome_date());
            p.setIncome(u.getIncome());
            userIncomePojos.add(p);
        }
        data.setList(userIncomePojos);
        data.setPage(event.getPage());
        data.setPageSize(event.getPageSize());
        long total =((Page)userIncomeInfos).getTotal();
        data.setTotal(total);
        data.setTotalPage(userIncomeInfos.isEmpty()?0:NumberUtil.Division(event.getPageSize(),total));
        return  data;

    }



    /**
     * 购买结果查询
     * @param paramMap
     *          参数集合
     * @return
     */
    private TradeResultQueryApply tradeResultQueryApply(Map<String, String> paramMap) {
        String url = appconfig.getURL_HS_TRADERESULTQUERY();
        url = MessageFormat.format(url, appconfig.getIP_PORT());
        TradeResultQueryApply apply = InterfaceCallingUtil.call(url, paramMap, InterfaceCallingUtil.POST, 0,
                TradeResultQueryApply.class);
        return apply;
    }


    /**
     * 份额查询
     * @param paramMap
     *          参数集合
     * @return
     */
    private ShareQueryReply shareQueryApply(Map<String, String> paramMap) {
        String url = appconfig.getURL_HS_SHARE_QUERY();
        url = MessageFormat.format(url, appconfig.getIP_PORT());
        ShareQueryReply apply = InterfaceCallingUtil.call(url, paramMap, InterfaceCallingUtil.POST, 0,
                ShareQueryReply.class);
        return apply;
    }


    /**
     * 购买申请查询
     * @param paramMap
     *          参数集合
     * @return
     */
    private TradeApplyQueryApply tradeApplyQueryApply(Map<String, String> paramMap) {
        String url = appconfig.getURL_HS_TRADEAPPLYQUERY();
        url = MessageFormat.format(url, appconfig.getIP_PORT());
        TradeApplyQueryApply apply = InterfaceCallingUtil.call(url, paramMap, InterfaceCallingUtil.POST, 0,
                TradeApplyQueryApply.class);
        return apply;
    }


    /**
     * 基金交易查询（申购，认购）
     * @param req
     *          参数
     * @return
     */
    public Msg queryHSOrderList(QueryHSOrderListEvent req) throws ParseException {
        Msg msg = new Msg();
        msg.setCode(Constant.FAIL);

        UserInfo  userInfo =userInfoMapper.findUserById(req.getUserId());
        //调用恒生接口查询 ---->  交易结果查询
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("sort_way", String.valueOf(1)); //倒序查询
        paramMap.put("client_id", userInfo.getClient_id());
        //交易结果查询
        TradeResultQueryApply result = tradeResultQueryApply(paramMap);
        if (null == result) {
            msg.setCode(Constant.FAIL);
            msg.setMsg("交易结果查询失败");
            return msg;
        }
        List<TradeResultQueryPo> tradeResultQueryPos=Arrays.asList(result.getTradeResultQuerys());
        if(tradeResultQueryPos==null||tradeResultQueryPos.isEmpty()){
            msg.setCode(Constant.FAIL);
            msg.setMsg("交易结果查询失败");
            return msg;
        }
        //获取该基金的交易结果
        tradeResultQueryPos =tradeResultQueryPos.stream().
                filter(o->o.getFund_code().equals(req.getFund_code())&&
                        o.getDeduct_status().equals(TradeEnums.HSDeductStatusEnum.YX.getValue())&&
                        (o.getFund_busin_code().equals(TradeEnums.HSBusinessApplyCodeEnum.SG.getValue())||
                                o.getFund_busin_code().equals(TradeEnums.HSBusinessApplyCodeEnum.RG.getValue()))&&
                        (o.getTrade_status().equals(TradeEnums.HSTradeStatusCharacterEnum.BFQR.getValue())||
                         o.getTrade_status().equals(TradeEnums.HSTradeStatusCharacterEnum.QRCG.getValue())||
                         o.getTrade_status().equals(TradeEnums.HSTradeStatusCharacterEnum.XWQR.getValue())||
                         o.getTrade_status().equals(TradeEnums.HSTradeStatusCharacterEnum.DQR.getValue())
                        )
                ).collect(Collectors.toList());
        if(tradeResultQueryPos==null||tradeResultQueryPos.isEmpty()){
            msg.setCode(Constant.FAIL);
            msg.setMsg("交易结果查询失败");
            return msg;
        }
        //用户订单
        List<HSOrderPojo> orderPojoList = new ArrayList<>();
        for(TradeResultQueryPo t :tradeResultQueryPos) {
            HSOrderPojo o =new HSOrderPojo();
            o.setAmount(t.getBalance());
            o.setConfirm_amount(t.getTrade_confirm_balance());
            o.setShares(t.getTrade_confirm_shares());
            o.setTimeStr(ORDER_Format.parse(t.getOrder_date_time()+"0"));
            if(t.getTrade_status().equals(
                    TradeEnums.HSTradeStatusCharacterEnum.BFQR.getValue())){
             o.setStatus("部分确认");
            }else if(t.getTrade_status().equals(TradeEnums.HSTradeStatusCharacterEnum.QRCG.getValue())){
             o.setStatus("确认成功");
            }else if( t.getTrade_status().equals(TradeEnums.HSTradeStatusCharacterEnum.XWQR.getValue())){
             o.setStatus("认购行为确认");
            }else if(t.getTrade_status().equals(TradeEnums.HSTradeStatusCharacterEnum.DQR.getValue())){
             o.setStatus("申请中");
            }
            o.setType(0);
            orderPojoList.add(o);
        }
        List<HSOrderPojo> pageList = new ArrayList<>();
        for (int i = (req.getPage() - 1) * req.getPageSize();
             i < (req.getPage() * req.getPageSize() > orderPojoList.size() ?
                     orderPojoList.size() : req.getPage() * req.getPageSize()); ++i){
            pageList.add(orderPojoList.get(i));
        }
        HSOrderListQueryEvent event = new HSOrderListQueryEvent();
        event.setList(pageList);
        event.setPage(req.getPage());
        event.setPageSize(req.getPageSize());

        msg.setCode(Constant.SUCCESS);
        msg.setData(event);
        msg.setMsg("查询数据成功");
        return msg;
    }


    /**
     * 基金交易查询(赎回)
     * @param req
     *          参数
     * @return
     */
    public Msg queryHSRedeeOrderList(QueryHSOrderListEvent req) throws ParseException {
        Msg msg = new Msg();
        msg.setCode(Constant.FAIL);
        UserInfo  userInfo =userInfoMapper.findUserById(req.getUserId());
        //调用恒生接口查询 ---->  交易结果查询
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("sort_way", String.valueOf(1)); //倒序查询
        paramMap.put("client_id", userInfo.getClient_id());
        //交易结果查询
        TradeResultQueryApply result = tradeResultQueryApply(paramMap);
        if (null == result) {
            msg.setCode(Constant.FAIL);
            msg.setMsg("交易结果查询失败");
            return msg;
        }
        List<TradeResultQueryPo> tradeResultQueryPos=Arrays.asList(result.getTradeResultQuerys());
        if(tradeResultQueryPos==null||tradeResultQueryPos.isEmpty()){
            msg.setCode(Constant.FAIL);
            msg.setMsg("赎回结果为空");
            return msg;
        }
        //获取该基金的交易结果
        tradeResultQueryPos =tradeResultQueryPos.stream().
                filter(o->(o.getFund_code().equals(req.getFund_code())&&
                        (o.getFund_busin_code().equals(TradeEnums.HSBusinessApplyCodeEnum.SH.getValue()))&&
                        (o.getTrade_status().equals(TradeEnums.HSTradeStatusCharacterEnum.BFQR.getValue())||
                                o.getTrade_status().equals(TradeEnums.HSTradeStatusCharacterEnum.QRCG.getValue())||
                                o.getTrade_status().equals(TradeEnums.HSTradeStatusCharacterEnum.DQR.getValue())||
                                o.getTrade_status().equals(TradeEnums.HSTradeStatusCharacterEnum.QRSB.getValue())
                        ))
                ).collect(Collectors.toList());
        if(tradeResultQueryPos==null||tradeResultQueryPos.isEmpty()){
            msg.setCode(Constant.FAIL);
            msg.setMsg("赎回结果为空");
            return msg;
        }
        //用户订单
        List<HSOrderPojo> orderPojoList = new ArrayList<>();
        for(TradeResultQueryPo t :tradeResultQueryPos) {
            HSOrderPojo o =new HSOrderPojo();
            o.setAmount(t.getBalance());
            o.setConfirm_amount(t.getTrade_confirm_balance());
            o.setShares(t.getApply_share());
            o.setTimeStr(ORDER_Format.parse(t.getOrder_date_time()+"0"));
            if(t.getTrade_status().equals(
                    TradeEnums.HSTradeStatusCharacterEnum.BFQR.getValue())){
                o.setStatus("部分赎回");
            }else if(t.getTrade_status().equals(TradeEnums.HSTradeStatusCharacterEnum.QRCG.getValue())){
                o.setStatus("已赎回");
            } else if(t.getTrade_status().equals(TradeEnums.HSTradeStatusCharacterEnum.DQR.getValue())){
                o.setStatus("赎回中");
            }else if(t.getTrade_status().equals(TradeEnums.HSTradeStatusCharacterEnum.QRSB.getValue())){
                o.setStatus("赎回失败");
            }
            o.setType(1);
            orderPojoList.add(o);
        }
        List<HSOrderPojo> pageList = new ArrayList<>();
        for (int i = (req.getPage() - 1) * req.getPageSize();
             i < (req.getPage() * req.getPageSize() > orderPojoList.size() ?
                     orderPojoList.size() : req.getPage() * req.getPageSize()); ++i){
            pageList.add(orderPojoList.get(i));
        }
        HSOrderListQueryEvent event = new HSOrderListQueryEvent();
        event.setList(pageList);
        event.setPage(req.getPage());
        event.setPageSize(req.getPageSize());

        msg.setCode(Constant.SUCCESS);
        msg.setData(event);
        msg.setMsg("查询数据成功");
        return msg;
    }

}
