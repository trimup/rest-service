package com.lihe.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lihe.AppConfig;
import com.lihe.common.Constant;
import com.lihe.common.Msg;
import com.lihe.entity.HSProductInfo;
import com.lihe.entity.UserInfo;
import com.lihe.entity.productDetail.*;
import com.lihe.event.FundHistoryNetData;
import com.lihe.event.FundTrendData;
import com.lihe.event.QueryPubProDetailEvent;
import com.lihe.hundSunreply.*;
import com.lihe.entity.productDetail.FundInfo;
import com.lihe.entity.productDetail.FundMainPositionsInfo;
import com.lihe.entity.productDetail.FundNetDayInfo;
import com.lihe.entity.productDetail.FundRateinfo;
import com.lihe.event.*;
import com.lihe.persistence.*;
import com.lihe.pojo.*;
import com.lihe.pojo.productDetail.*;
import com.lihe.until.DateUtil;
import com.lihe.until.InterfaceCallingUtil;
import com.lihe.until.NumberUtil;
import com.lihe.until.PHPSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by trimup on 2016/8/23.
 */
@Service
public class ProductDetailService {

    private static final Logger L = LoggerFactory.getLogger(ProductDetailService.class);
    private static final SimpleDateFormat Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat Trade_Format = new SimpleDateFormat("yyyyMMddHHmmssS");
    private static final SimpleDateFormat CONFRIM_Format = new SimpleDateFormat("yyyyMMdd");
    private static final SimpleDateFormat Date_Format = new SimpleDateFormat("yyyy-MM-dd");


    @Autowired
    private HSProductMapper hsProductMapper;
    @Autowired
    private FundInfoMapper fundInfoMapper;
    @Autowired
    private FundNetDayMapper fundNetDayMapper;
    @Autowired
    private FundRateMapper fundRateMapper;
    @Autowired
    private FundMainPositionMapper fundMainPositionMapper;
    @Autowired
    private FundIncomeMapper fundIncomeMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private AppConfig appConfig;
    @Autowired
    private FundAssetAllocationMapper fundAssetAllocationMapper;



    /**
     * 获取公募基金信息
     * @return
     */
   public Msg getPubProInfo(String fund_code) {
       FundInfo fundInfo
                = fundInfoMapper.queryFundByCode(fund_code);
       HSProductInfo hsProductInfo =hsProductMapper.queryHSProductByFundCode(fund_code);
       //查询该基金每日净值
       List<FundNetDayInfo>  netDayList =fundNetDayMapper.getFundNetDay(fund_code);
       PubFundPojo pubFundPojo  =new PubFundPojo();
       //该基金最新的净值
       if(!netDayList.isEmpty())
       {
           FundNetDayInfo  todayNet =netDayList.get(0);
           pubFundPojo.setYzdf(todayNet.getSyl_y()); //月涨跌幅
           pubFundPojo.setJzdf(todayNet.getSyl_3y()); //季度涨跌幅
           pubFundPojo.setBnzdf(todayNet.getSyl_6y());  //半年涨跌幅
           pubFundPojo.setNzdf(todayNet.getSyl_1n()); //年涨跌幅
           pubFundPojo.setNetValue(todayNet.getDwjz());
           pubFundPojo.setHqrq(todayNet.getHqrq());
           pubFundPojo.setRzdf(todayNet.getRzdf());

       }

      // 查询出认购前端的税率
       List<FundRateinfo>  rateinfos =fundRateMapper.queryFundRate(fund_code,1);
       if(!rateinfos.isEmpty()){
           FundRateinfo rateinfo =rateinfos.stream().sorted((a,b)->b.getPreferred_rate().
                   compareTo(a.getPreferred_rate())).collect(Collectors.toList()).get(0);
           pubFundPojo.setBuyRate(rateinfo.getPreferred_rate()); //折后购买费率
           pubFundPojo.setOrgBuyRate(rateinfo.getOrgiginal_rate()); //原始购买费率
       }
       pubFundPojo.setId(hsProductInfo.getId());
       pubFundPojo.setFund_code(hsProductInfo.getFund_code());
       pubFundPojo.setFund_full_name(hsProductInfo.getFund_full_name());
       pubFundPojo.setStartMoney(hsProductInfo.getStart_money());
       pubFundPojo.setFund_status(hsProductInfo.getFund_status());
       pubFundPojo.setFundStatus(hsProductInfo.getFundStatus());
       Msg msg =new Msg();
       msg.setCode(Constant.SUCCESS);
       msg.setData(pubFundPojo);
       msg.setMsg(Constant.SUCCESS_MSG);
       return  msg;
   }

    /**
     * 获取公募基金历史净值
     */
    public FundHistoryNetData getFundHistoryNet(QueryPubProDetailEvent event){
        FundHistoryNetData data =new FundHistoryNetData();
        PageHelper.startPage(event.getPage(),event.getPageSize());
        List<FundNetDayInfo> infos =fundNetDayMapper.getFundNetDay(event.getFund_code());
        List<FundNetDayPojo> pojos =new ArrayList<>();
        for(FundNetDayInfo i :infos){
            FundNetDayPojo p =new FundNetDayPojo();
            p.setDwjz(i.getDwjz());
            p.setHqrq(i.getHqrq());
            p.setRzdf(i.getRzdf());
            pojos.add(p);
        }
        data.setList(pojos);
        data.setPage(event.getPage());
        data.setPageSize(event.getPageSize());
        long total =((Page)infos).getTotal();
        data.setTotal(total);
        data.setTotalPage(NumberUtil.Division(event.getPageSize(),total));
        return data;
    }


    /**
     * 获取公募基金 走势
     */
    public Msg getFundTrend(String fund_code,Integer month) throws Exception {
        Msg msg =new Msg();
        int timelimit =DateUtil.getCountDayBeforeMonth()
        FundTrendData data =new FundTrendData();
        List<FundNetDayInfo> infos =fundNetDayMapper.getFundNetDay(fund_code);
        List<String>  dateList =new ArrayList<>();  //日期list
        List<String> netValueList=new ArrayList<>();//净值list
        if(infos.size()<timelimit)
            return  new Msg(Constant.FAIL,"无该基金走势信息");
        infos =infos.stream().limit(timelimit).collect(Collectors.toList());
        infos=infos.stream().sorted((o1,o2)->o1.getHqrqDate().compareTo(o2.getHqrqDate())).collect(Collectors.toList());
        for(int i=0;i<infos.size();i++){
            FundNetDayInfo f =infos.get(i);
            dateList.add(f.getHqrq());     //行情日期
            netValueList.add(f.getDwjz());   //单位净值
        }
        data.setDateList(dateList);
        data.setNetValueList(netValueList);
        msg.setCode(Constant.SUCCESS);
        msg.setData(data);
        msg.setMsg(Constant.SUCCESS_MSG);
        return msg;
    }





    /**
     * 获取公募基金的基本信息
     * @param fund_code
     * @return
     */
    public Msg getPubProBaseDetail(String fund_code)  {
        HSProductInfo hsProductInfo = hsProductMapper.queryHSProductByFundCode(fund_code);
        if(hsProductInfo==null)
            return  new Msg(Constant.FAIL,"无该基金信息");
        FundInfo fundInfo =fundInfoMapper.queryFundByCode(fund_code);
        if(fundInfo==null)
            return  new Msg(Constant.FAIL,"无该基金信息");
        //查询该基金每日净值
        List<FundNetDayInfo>  netDayList =fundNetDayMapper.getFundNetDay(fund_code);
        //该基金最新的净值

        List<FundMainPositionsInfo> fundMainPostList = fundMainPositionMapper.queryFundMainPostion(fund_code);
        //如果主要持持仓  人数大于4  取 占配比最大的 4家
        if(fundMainPostList.size()>4){
            fundMainPostList=fundMainPostList.stream().sorted((o1,o2)->new Float(
                    o2.getAccounting_ratio().substring(0,o2.getAccounting_ratio().indexOf("%"))).compareTo(new Float(
                    o1.getAccounting_ratio().substring(0,o1.getAccounting_ratio().indexOf("%"))))).limit(4).collect(Collectors.toList());
        }
        PubProductBasePojo basePojo =new PubProductBasePojo();
        basePojo.setFund_code(hsProductInfo.getFund_code());
        basePojo.setFund_full_name(hsProductInfo.getFund_full_name());
        basePojo.setFund_share(fundInfo.getTotal_share());
        basePojo.setFund_status(hsProductInfo.getFundStatus());
        basePojo.setNet_asset_value(fundInfo.getNet_asset_value());
        basePojo.setOfund_risklevel(hsProductInfo.getOfund_risklevel());
        basePojo.setOfundRisk(hsProductInfo.getOfundRisk());
        basePojo.setOfund_type(hsProductInfo.getOfundType());
        basePojo.setSetup_time(fundInfo.getSetup_time());
        basePojo.setTzfw(fundInfo.getInvest_scope());
        basePojo.setTzmb(fundInfo.getInvest_target());
        basePojo.setFund_management(fundInfo.getFund_management());
        FundAssetAllocationInfo allocationInfo= fundAssetAllocationMapper.queryAssetAllocationByCode(fund_code);
        AssetAllocationPojo aapojo=new AssetAllocationPojo();
        if(allocationInfo==null){
            aapojo.setGp("---");
            aapojo.setYh("---");
            aapojo.setZq("---");
            aapojo.setZgm("---");
        }else {
            aapojo.setGp(allocationInfo.getStock_per());
            aapojo.setYh(allocationInfo.getBank_per());
            aapojo.setZq(allocationInfo.getBond_per());
            aapojo.setZgm(allocationInfo.getTotal_assets());
        }

        basePojo.setAssetAllocationPojo(aapojo);//资产配置
        basePojo.setMainPositionsList(fundMainPostList); //主要持仓
        basePojo.setCustodial_bank(fundInfo.getCustodian_bank());
        basePojo.setSetup_time(fundInfo.getSetup_time());
        Msg msg =new Msg();
        msg.setCode(Constant.SUCCESS);
        msg.setData(basePojo);
        msg.setMsg(Constant.SUCCESS_MSG);
        return  msg;
    }




    /**
     * 获取公募基金的基金经理
     * @param fund_code
     * @return
     */
    public Msg getPubProFundMananger(String fund_code) throws IllegalAccessException {
        FundInfo fundInfo =fundInfoMapper.queryFundByCode(fund_code);
        if(fundInfo==null)
            return  new Msg(Constant.FAIL,"无该基金信息");
        FundManagerPojo fmPojo =new FundManagerPojo();
        fmPojo.setJlllogo(fundInfo.getFund_manager_logo());
        fmPojo.setJlnr(fundInfo.getFund_manager_introduce());
        fmPojo.setJlxm(fundInfo.getFund_manager_name());
        Msg msg =new Msg();
        msg.setCode(Constant.SUCCESS);
        msg.setData(fmPojo);
        msg.setMsg(Constant.SUCCESS_MSG);
        return   msg;
    }


    /**
     * 获取公募基金的费率
     * @param fund_code
     * @return
     */
    public Msg getPubProFundRate(String fund_code) throws IllegalAccessException {
        HSProductInfo hsProductInfo =hsProductMapper.queryHSProductByFundCode(fund_code);
        FundInfo fundInfo =fundInfoMapper.queryFundByCode(fund_code);
        if(fundInfo==null)
            return  new Msg(Constant.FAIL,"无该基金信息");
        FundRateData data =new FundRateData();
        List<FundRateinfo>  purchaseRateList =fundRateMapper.queryFundRate(fund_code,1);
        List<FundRateinfo>  redeemRateList =fundRateMapper.queryFundRate(fund_code,5);

        Map fl=(Map) PHPSerializer.unserialize(hsProductInfo.getFl().getBytes());
        if(!CollectionUtils.isEmpty(fl)&&fl!=null){
            data.setFund_manager_rate((String)fl.get("jjglf"));
            data.setFund_trust_rate(fundInfo.getFund_trust_rate());
            data.setPu_confir_time((String)fl.get("sqsj"));
            data.setSale_service_rate((String)fl.get("xsfwf"));
        }
        List<PurchaseRatePojo> purRateList =new ArrayList<>();
        for(FundRateinfo f :purchaseRateList){
            PurchaseRatePojo p =new PurchaseRatePojo();
            p.setAmount_range(f.getAmount_range());
            p.setOrgiginal_rate(f.getOrgiginal_rate());
            p.setPreferred_rate(f.getPreferred_rate());
            purRateList.add(p);
        }
        List<RedeemRatePojo> redeemRatePojos =new ArrayList<>();
        for(FundRateinfo f : redeemRateList){
            RedeemRatePojo r =new RedeemRatePojo();
            r.setRate(f.getPreferred_rate());
            r.setTime_range(f.getAmount_range());
            redeemRatePojos.add(r);
        }
        data.setPurchase_rate(purRateList);
        data.setRedeem_confir_time((String)fl.get("shsj"));
        data.setRedeem_rate(redeemRatePojos);
        Msg msg =new Msg();
        msg.setCode(Constant.SUCCESS);
        msg.setData(data);
        msg.setMsg(Constant.SUCCESS_MSG);
        return   msg;
    }

    /**
     * 获取公募基金的赎回费率信息
     * @param fund_code
     *          基金编码
     * @return
     */
    public Msg queryPubFundRateForRedeem(String fund_code) {
        PubFundRateForRedeemQueryEvent event = new PubFundRateForRedeemQueryEvent();
        List<FundRateinfo>  redeemRateList = fundRateMapper.queryFundRate(fund_code,5);
        event.setRateRange("0%");
        List<RedeemRatePojo> redeemRatePojoList =new ArrayList<>();
        if (redeemRateList.size() > 0) {
            redeemRateList.stream().forEach(r -> {
                RedeemRatePojo po = new RedeemRatePojo();
                po.setRate(r.getPreferred_rate());
                po.setTime_range(r.getAmount_range());
                redeemRatePojoList.add(po);
            });
            //正则表达式，匹配 1.23% 格式的数据
            BigDecimal min = redeemRateList.stream()
                    .filter(r -> null != r.getPreferred_rate()
                            && r.getPreferred_rate().matches("([1-9][0-9]*\\.[0-9]*|0\\.[0-9]*[1-9][0-9]*)%"))
                    .map(r -> new BigDecimal(r.getPreferred_rate().replace("%", "")))
                    .min((x, y) -> x.compareTo(y)).orElse(new BigDecimal("-1"));

            BigDecimal max = redeemRateList.stream()
                    .filter(r -> null != r.getPreferred_rate()
                            && r.getPreferred_rate().matches("([1-9][0-9]*\\.[0-9]*|0\\.[0-9]*[1-9][0-9]*)%"))
                    .map(r -> new BigDecimal(r.getPreferred_rate().replace("%", "")))
                    .max((x, y) -> x.compareTo(y)).orElse(new BigDecimal("-1"));

            if (min.compareTo(new BigDecimal("-1")) != 0 && max.compareTo(new BigDecimal("-1")) != 0) {
                event.setRateRange(min + "%-" + max + "%");
            }
        }
        event.setList(redeemRatePojoList);

        Msg msg =new Msg();
        msg.setCode(Constant.SUCCESS);
        msg.setData(event);
        msg.setMsg(Constant.SUCCESS_MSG);
        return   msg;
    }

    /**
     * 持有基金详情(已持仓)
     * @param fund_code
     * @return
     */
    public Msg holdFundDetail(String fund_code,Integer user_tid) throws ParseException {
        Msg msg =new Msg();
        HoldFundPojo holdFundPojo=new HoldFundPojo();
        HSProductInfo  hsProductInfo =hsProductMapper.queryHSProductByFundCode(fund_code);
        UserInfo userInfo =userService.getUserInfoById(user_tid);
        List<FundIncomeInfo> fundIncomeInfos =fundIncomeMapper.
                queryFundIncomeByUser(fund_code,user_tid);
        //基金代码
        holdFundPojo.setFund_id(hsProductInfo.getId());
        holdFundPojo.setFund_code(fund_code);
        //基金全名
        holdFundPojo.setFund_full_name(hsProductInfo.getFund_full_name());
        holdFundPojo.setFundType(hsProductInfo.getOfundType());

        List<FundNetDayInfo>  netDayList =fundNetDayMapper.getFundNetDay(fund_code);
        //基金跌涨幅
        if(netDayList==null||netDayList.isEmpty()){
            holdFundPojo.setFallAndRise("---");
            holdFundPojo.setYzdf("---");
            holdFundPojo.setJzdf("---");
            holdFundPojo.setBnzdf("---");
            holdFundPojo.setNzdf("---");
        }else{
            FundNetDayInfo todayNet = netDayList.get(0);
            holdFundPojo.setFallAndRise(todayNet.getRzdf());
            holdFundPojo.setYzdf(todayNet.getSyl_y());
            holdFundPojo.setJzdf(todayNet.getSyl_3y());
            holdFundPojo.setBnzdf(todayNet.getSyl_6y());
            holdFundPojo.setNzdf(todayNet.getSyl_1n());

        }
        Map<String, String> shareQueryParamMap = new HashMap<>();
        shareQueryParamMap.put("client_id", userInfo.getClient_id() );
        shareQueryParamMap.put("fund_code",fund_code);
        shareQueryParamMap.put("isFilter", "1");
        ShareQueryReply reply =shareQueryApply(shareQueryParamMap);
        if(reply.getCode().equals(Constant.HS_SUCCESS_CODE)){
            if(reply.getShareQuerys()!=null&&!reply.getShareQuerys().isEmpty()){
                ShareQueryPo shareQueryPo =reply.getShareQuerys().get(0);
                holdFundPojo.setTotalProfit(shareQueryPo.getAccum_income().toString());
                holdFundPojo.setProfit_date(shareQueryPo.getNet_value_date());
            }
        }
        //用户累计收益
        BigDecimal cumulative_profit =fundIncomeMapper.CumulativeProfitFundIncome(fund_code,user_tid);
        String yesterday =Format.format(DateUtil.getDayBefore(new Date()));
        BigDecimal profit=fundIncomeMapper.queryFundIncomeByDate(fund_code,user_tid,yesterday);
        //用户的昨日收益
        holdFundPojo.setTodayProfit(profit==null?"0":profit.toString());
        holdFundPojo.setProfit_date(yesterday);
        holdFundPojo.setTotalProfit(cumulative_profit.toString());
        //用户成交记录查询 查询出最近成交一笔交易时间
        Map<String, String> confirmPramMap = new HashMap<>();
        confirmPramMap.put("client_id", userInfo.getClient_id());
        confirmPramMap.put("fund_code", fund_code);
        confirmPramMap.put("fund_busin_code","122"); //申购
        TradeConfirmReply tradeComfirmQueryApply =tradeComfirmQueryApply(confirmPramMap);
        if(tradeComfirmQueryApply.getCode().equals(Constant.HS_SUCCESS_CODE)){
            if(tradeComfirmQueryApply.getTradeConfirmQuerys()!=null&&!tradeComfirmQueryApply.getTradeConfirmQuerys().isEmpty()){
                List<TradeconfirmBean> sortResult=
                        tradeComfirmQueryApply.getTradeConfirmQuerys().stream().sorted(
                                (o1, o2) -> o2.getAccept_time().compareTo(o2.getAccept_time())).collect(Collectors.toList());
                holdFundPojo.setTrade_time(Format.format(CONFRIM_Format.parse(sortResult.get(0).getAffirm_date().toString())));
            }
        }
        //分红方式
        boolean  allow_fix_bonus =false;
        //查询该用户的可修改分红方式列表
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<String, String>();
        HttpHeaders requestHeaders = new HttpHeaders();
        paramMap.add("client_id",userInfo.getClient_id());
        RestTemplate restTemplate = new RestTemplate();
        String url = appConfig.getURL_HS_BONUSLIST_QUERY();
        url = MessageFormat.format(url, appConfig.getIP_PORT());
        BonusListQueryReply bonusListQueryReply  =
                restTemplate.postForObject(url, paramMap,BonusListQueryReply.class);
        L.info("query bonuslist return is   "+bonusListQueryReply.toString());
        if(bonusListQueryReply.getCode().equals(Constant.HS_SUCCESS_CODE)){
            if(bonusListQueryReply.getBonusListQueries()!=null&&!bonusListQueryReply.getBonusListQueries().isEmpty()){
                for(BonusListBean b : bonusListQueryReply.getBonusListQueries()){
                    if(b.getFund_code().equals(fund_code))
                        allow_fix_bonus=true;
                        holdFundPojo.setAuto_buy(b.getAuto_buy());

                }
            }
        }
        holdFundPojo.setAllow_fix_bonus(allow_fix_bonus);
        holdFundPojo.setOfundRisk(hsProductInfo.getOfundRisk());
        holdFundPojo.setOfund_risklevel(hsProductInfo.getOfund_risklevel());

        msg.setData(holdFundPojo);
        msg.setCode(Constant.SUCCESS);
        msg.setMsg(Constant.SUCCESS_MSG);
        return  msg;
    }


    /**
     * 申请中的基金详情
     * @param fund_code
     * @return
     */
    public Msg applyingFundDetail(String fund_code,Integer user_tid,String allot_no) throws ParseException {
        Msg msg =new Msg();
        HoldFundPojo holdFundPojo=new HoldFundPojo();
        HSProductInfo  hsProductInfo =hsProductMapper.queryHSProductByFundCode(fund_code);
        //基金代码
        holdFundPojo.setFund_id(hsProductInfo.getId());
        holdFundPojo.setFund_code(fund_code);
        //基金全名
        holdFundPojo.setFund_full_name(hsProductInfo.getFund_full_name());
        //基金累计收益
        holdFundPojo.setTotalProfit("---");
        holdFundPojo.setProfit_date("---");
        holdFundPojo.setTodayProfit("---");
        //基金类型
        holdFundPojo.setFundType(hsProductInfo.getOfundType());
        List<FundNetDayInfo>  netDayList =fundNetDayMapper.getFundNetDay(fund_code);
        //基金跌涨幅
        if(netDayList==null||netDayList.isEmpty()){
            holdFundPojo.setFallAndRise("---");
            holdFundPojo.setYzdf("---");
            holdFundPojo.setJzdf("---");
            holdFundPojo.setBnzdf("---");
            holdFundPojo.setNzdf("---");
        }else{
            FundNetDayInfo todayNet = netDayList.get(0);
            holdFundPojo.setFallAndRise(todayNet.getRzdf());
            holdFundPojo.setYzdf(todayNet.getSyl_y());
            holdFundPojo.setJzdf(todayNet.getSyl_3y());
            holdFundPojo.setBnzdf(todayNet.getSyl_6y());
            holdFundPojo.setNzdf(todayNet.getSyl_1n());

        }
        UserInfo userInfo =userService.getUserInfoById(user_tid);
        //用户成交记录查询
        Map<String, String> resultPramMap = new HashMap<>();
        resultPramMap.put("client_id", userInfo.getClient_id());
        resultPramMap.put("allot_no", allot_no);
        TradeResultQueryApply result = tradeResultQueryApply(resultPramMap);
        if(result.getCode().equals(Constant.HS_SUCCESS_CODE)){
            List<TradeResultQueryPo> tradeResultQueryPos=Arrays.asList(result.getTradeResultQuerys());
            if(!tradeResultQueryPos.isEmpty()){
                List<TradeResultQueryPo> sortResult=
                        tradeResultQueryPos.stream().sorted(
                                (o1, o2) -> o2.getOrder_date_time().compareTo(o2.getOrder_date_time())).collect(Collectors.toList());
                holdFundPojo.setTrade_time(Format.format(Trade_Format.parse(sortResult.get(0).getOrder_date_time()+"0")));
            }

        }
        holdFundPojo.setOfundRisk(hsProductInfo.getOfundRisk());
        holdFundPojo.setOfund_risklevel(hsProductInfo.getOfund_risklevel());
        msg.setData(holdFundPojo);
        msg.setCode(Constant.SUCCESS);
        msg.setMsg(Constant.SUCCESS_MSG);
        return  msg;
    }

    /**
     * 认购的基金详情
     * @param fund_code
     * @return
     */
    public Msg subsFundDetail(String fund_code,Integer user_tid) throws ParseException {
        Msg msg =new Msg();
        HoldFundPojo holdFundPojo=new HoldFundPojo();
        HSProductInfo  hsProductInfo =hsProductMapper.queryHSProductByFundCode(fund_code);
        //基金代码
        holdFundPojo.setFund_id(hsProductInfo.getId());
        holdFundPojo.setFund_code(fund_code);
        //基金全名
        holdFundPojo.setFund_full_name(hsProductInfo.getFund_full_name());
        //基金累计收益
        holdFundPojo.setTotalProfit("---");
        holdFundPojo.setProfit_date("---");
        holdFundPojo.setTodayProfit("---");
        //基金类型
        holdFundPojo.setFundType(hsProductInfo.getOfundType());
        List<FundNetDayInfo>  netDayList =fundNetDayMapper.getFundNetDay(fund_code);
        //基金跌涨幅
        if(netDayList==null||netDayList.isEmpty()){
            holdFundPojo.setFallAndRise("---");
            holdFundPojo.setYzdf("---");
            holdFundPojo.setJzdf("---");
            holdFundPojo.setBnzdf("---");
            holdFundPojo.setNzdf("---");
        }else{
            FundNetDayInfo todayNet = netDayList.get(0);
            holdFundPojo.setFallAndRise(todayNet.getRzdf());
            holdFundPojo.setYzdf(todayNet.getSyl_y());
            holdFundPojo.setJzdf(todayNet.getSyl_3y());
            holdFundPojo.setBnzdf(todayNet.getSyl_6y());
            holdFundPojo.setNzdf(todayNet.getSyl_1n());

        }
        UserInfo userInfo =userService.getUserInfoById(user_tid);
        //用户成交记录查询
        Map<String, String> resultPramMap = new HashMap<>();
        resultPramMap.put("businflag", "020"); //认购
        resultPramMap.put("client_id", userInfo.getClient_id());
        resultPramMap.put("confirm_state", "5");
        resultPramMap.put("sort_way", "1");
        TradeResultQueryApply result = tradeResultQueryApply(resultPramMap);
        if(result.getCode().equals(Constant.HS_SUCCESS_CODE)){
            List<TradeResultQueryPo> tradeResultQueryPos=Arrays.asList(result.getTradeResultQuerys());
            if(!tradeResultQueryPos.isEmpty()){
                List<TradeResultQueryPo> sortResult=
                        tradeResultQueryPos.stream().filter(o->o.getFund_code().equals(fund_code)).collect(Collectors.toList());
                if(sortResult!=null||!sortResult.isEmpty()){
                    sortResult=sortResult.stream().sorted(
                            (o1, o2) -> o2.getOrder_date_time().compareTo(o2.getOrder_date_time())).collect(Collectors.toList());
                    holdFundPojo.setTrade_time(Format.format(Trade_Format.parse(sortResult.get(0).getOrder_date_time()+"0")));
                }
            }
        }
        holdFundPojo.setOfundRisk(hsProductInfo.getOfundRisk());
        holdFundPojo.setOfund_risklevel(hsProductInfo.getOfund_risklevel());
        msg.setData(holdFundPojo);
        msg.setCode(Constant.SUCCESS);
        msg.setMsg(Constant.SUCCESS_MSG);
        return  msg;
    }




    /**
     * 购买结果查询
     * @param paramMap
     *          参数集合
     * @return
     */
    private TradeConfirmReply tradeComfirmQueryApply(Map<String, String> paramMap) {
        String url = appConfig.getURL_HS_TRADECONFIRMQUERY() ;
        url = MessageFormat.format(url, appConfig.getIP_PORT());
        TradeConfirmReply apply = InterfaceCallingUtil.call(url, paramMap, InterfaceCallingUtil.POST, 0,
                TradeConfirmReply.class);
        return apply;
    }

    /**
     * 购买结果查询
     * @param paramMap
     *          参数集合
     * @return
     */
    private TradeResultQueryApply tradeResultQueryApply(Map<String, String> paramMap) {
        String url =appConfig.getURL_HS_TRADERESULTQUERY();
        url = MessageFormat.format(url, appConfig.getIP_PORT());
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
        String url =appConfig.getURL_HS_SHARE_QUERY() ;
        url = MessageFormat.format(url, appConfig.getIP_PORT());
        ShareQueryReply apply = InterfaceCallingUtil.call(url, paramMap, InterfaceCallingUtil.POST, 0,
                ShareQueryReply.class);
        return apply;
    }

}
