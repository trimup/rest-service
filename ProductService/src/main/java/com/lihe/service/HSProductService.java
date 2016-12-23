package com.lihe.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.base.Strings;
import com.lihe.common.Constant;
import com.lihe.common.Msg;
import com.lihe.entity.ConcernedProdcutInfo;
import com.lihe.entity.HSProductInfo;
import com.lihe.entity.PrivateProductInfo;
import com.lihe.entity.RecommendProductInfo;
import com.lihe.entity.productDetail.FundInfo;
import com.lihe.entity.productDetail.FundNetDayInfo;
import com.lihe.entity.productDetail.SunNetValInfo;
import com.lihe.event.*;
import com.lihe.persistence.*;
import com.lihe.pojo.*;
import com.lihe.pojo.productDetail.SunProductPojo;
import com.lihe.until.NumberUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by trimup on 2016/8/9.
 */
@Service
public class HSProductService {

    private static final Logger L = LoggerFactory.getLogger(HSProductService.class);

    private static final SimpleDateFormat Format = new SimpleDateFormat("yyyy-MM-dd");



    @Autowired
    private HSProductMapper hsProductMapper;

    @Autowired
    private PrivateProductMapper privateProductMapper;

    @Autowired
    private FundNetDayMapper fundNetDayMapper;
    @Autowired
    private RecoProductMapper recoProductMapper;
    @Autowired
    private ConcernedMapper concernedMapper;

    @Autowired
    private SunNetValMapper sunNetValMapper;



    /**
     * 查询公募基金列表
     * @return
     */
    public HSProListData queryPublicProduct(QueryHSProEvent event){
        HSProListData hsProListReturn =new HSProListData();
        //查询出今天的最近历史基金行情
        List<HSProductInfo>  hsProductInfos = hsProductMapper.queryHSProduct(event.getOfund_type());
        List<HSListPojo> listPojos =new ArrayList<>();
        for(HSProductInfo hs : hsProductInfos){
            HSListPojo p =new HSListPojo();
            p.setStart_money(hs.getStart_money());
            p.setFund_code(hs.getFund_code()); //基金代码
            p.setFund_name(hs.getFund_name()); //基金名称
            p.setFund_curr_ratio(hs.getFund_curr_ratio());  //七日年化收益
            p.setPer_myriad_income(hs.getPer_myriad_income()); //万份基金收益
            p.setNet_value(hs.getNet_value()); //基金净值
            p.setFundType(hs.getOfundType());
            //查询该基金 涨跌幅度
            //查询该基金每日净值
            List<FundNetDayInfo>  netDayList =fundNetDayMapper.getFundNetDay(hs.getFund_code());
            //该基金最新的净值
            if(!netDayList.isEmpty()) {
                FundNetDayInfo todayNet = netDayList.get(0);

                if(NumberUtil.isPercent(todayNet.getRzdf())){
                    p.setDayFallRise(Double.parseDouble(todayNet.getRzdf().substring(0, todayNet.getRzdf().length() - 1)));
                }
                p.setDayFallRisePer(todayNet.getRzdf());
                if(NumberUtil.isPercent(todayNet.getSyl_y())){
                    p.setMonthFallRise(Double.parseDouble(todayNet.getSyl_y().substring(0, todayNet.getSyl_y().length() - 1)));
                }
                p.setMonthFallRisePer(todayNet.getSyl_y());
                if(NumberUtil.isPercent(todayNet.getSyl_6y())){
                    p.setHalfYearFallRise(Double.parseDouble(todayNet.getSyl_6y().substring(0, todayNet.getSyl_6y().length() - 1)));
                }
                p.setHalfYearFallRisePer(todayNet.getSyl_6y());
                if(NumberUtil.isPercent(todayNet.getSyl_1n())){
                    p.setYearFallRise(Double.parseDouble(todayNet.getSyl_1n().substring(0, todayNet.getSyl_1n().length() - 1)));
                }
                p.setYearFallRisePer(todayNet.getSyl_1n());
                if(NumberUtil.isPercent(todayNet.getSyl_3y())){
                    p.setQuarterFallRise(Double.parseDouble(todayNet.getSyl_3y().substring(0, todayNet.getSyl_3y().length() - 1)));
                }
                p.setQuarterFallRisePer(todayNet.getSyl_3y());
            }
//            calFundFallRise(p);
            listPojos.add(p);
        }
        List<HSListPojo> sortList =new ArrayList<>();

        //按照 日 月 季度 半年的 涨幅度排序
        switch (event.getSort_type()){
            case 0 :
                sortList=listPojos.stream().sorted((x,y)->
                        y.getDayFallRise().compareTo(x.getDayFallRise())).collect(Collectors.toList());
                break;
            case 1 :
                sortList=listPojos.stream().sorted((x,y)->
                        y.getMonthFallRise().compareTo(x.getMonthFallRise())).collect(Collectors.toList());
                break;
            case 2 :
                sortList=listPojos.stream().sorted((x,y)->
                        y.getQuarterFallRise().compareTo(x.getQuarterFallRise())).collect(Collectors.toList());
                break;
            case 3 :
                sortList=listPojos.stream().sorted((x,y)->
                        y.getHalfYearFallRise().compareTo(x.getHalfYearFallRise())).collect(Collectors.toList());
                break;
            case 4 :
                sortList=listPojos.stream().sorted((x,y)->
                        y.getYearFallRise().compareTo(x.getYearFallRise())).collect(Collectors.toList());
                break;
            default:
                sortList = listPojos.stream().sorted((x,y)->
                        y.getDayFallRise().compareTo(x.getDayFallRise())).collect(Collectors.toList());
        }

        List<FundListPojo> pageList =new ArrayList<>();
        for (int i = (event.getPage() - 1) * event.getPageSize();
             i < (event.getPage() * event.getPageSize() > sortList.size() ?
                     sortList.size() : event.getPage() * event.getPageSize()); ++i) {

            FundListPojo f =new FundListPojo();
            f.setFund_name(sortList.get(i).getFund_name());
            f.setFund_code(sortList.get(i).getFund_code());
            f.setPer_myriad_income(sortList.get(i).getPer_myriad_income());
            f.setNet_value(sortList.get(i).getNet_value()) ;
            f.setFund_curr_ratio(sortList.get(i).getFund_curr_ratio());
            f.setStart_money(sortList.get(i).getStart_money());
            f.setDayFallRisePer(sortList.get(i).getDayFallRisePer());
            f.setMonthFallRisePer(sortList.get(i).getMonthFallRisePer());
            f.setHalfYearFallRisePer(sortList.get(i).getHalfYearFallRisePer());
            f.setYearFallRisePer(sortList.get(i).getYearFallRisePer()) ;
            f.setQuarterFallRisePer(sortList.get(i).getQuarterFallRisePer());
            f.setFund_type(sortList.get(i).getFundType());
            pageList.add(f);
        }
        hsProListReturn.setList(pageList);
        hsProListReturn.setPage(event.getPage());
        hsProListReturn.setPageSize(event.getPageSize());
        hsProListReturn.setTotal(sortList.size());
        hsProListReturn.setTotalPage(NumberUtil.Division(event.getPageSize(), sortList.size()));
        return hsProListReturn;
    }



    /**
     * 计算基金涨幅
     */
    public  void  calFundFallRise(HSListPojo p){
        List<HSProductInfo> fundHistoryPojos =hsProductMapper.queryHSHistoryLog(p.getFund_code());
        if(fundHistoryPojos==null||fundHistoryPojos.size()<2){
            p.setDayFallRisePer("--");
            p.setQuarterFallRisePer("--");
            p.setMonthFallRisePer("--");
            p.setHalfYearFallRisePer("--");
            p.setYearFallRisePer("--");
            p.setDayFallRise(0d);
            p.setMonthFallRise(0d);
            p.setQuarterFallRise(0d);
            p.setHalfYearFallRise(0d);
            p.setYearFallRise(0d);
        }else{
            //日跌涨幅度
            double  dayFallRise = fundHistoryPojos.get(0).getNet_value().subtract(fundHistoryPojos.get(1).getNet_value()).divide(fundHistoryPojos.get(1).getNet_value())
                    .doubleValue();
            NumberFormat nf  =  NumberFormat.getPercentInstance();
            nf.setMinimumFractionDigits( 2 );
            p.setDayFallRisePer(nf.format(dayFallRise));
            p.setDayFallRise(dayFallRise);
            //月涨跌幅度
            double  monthFallRise=0;
            for(int i=1;i<(fundHistoryPojos.size()<=30 ?fundHistoryPojos.size():30);i++){
                monthFallRise=monthFallRise+
                        (fundHistoryPojos.get(i-1).getNet_value().subtract(fundHistoryPojos.get(i).getNet_value())).
                                divide(fundHistoryPojos.get(i).getNet_value()).doubleValue();
            }
            p.setMonthFallRise(monthFallRise);
            p.setMonthFallRisePer(nf.format(monthFallRise));
            //季度涨跌幅度
            double  quarterFallRise=0;
            for(int i=1;i<(fundHistoryPojos.size()<=90 ?fundHistoryPojos.size():90);i++){
                quarterFallRise=quarterFallRise+
                        (fundHistoryPojos.get(i-1).getNet_value().subtract(fundHistoryPojos.get(i).getNet_value())).
                                divide(fundHistoryPojos.get(i).getNet_value()).doubleValue();
            }
            p.setQuarterFallRise(quarterFallRise);
            p.setQuarterFallRisePer(nf.format(quarterFallRise));
            //半年涨跌幅度
            double  halfYearFallRise=0;
            for(int i=1;i<(fundHistoryPojos.size()<=90 ?fundHistoryPojos.size():90);i++){
                halfYearFallRise=halfYearFallRise+
                        (fundHistoryPojos.get(i-1).getNet_value().subtract(fundHistoryPojos.get(i).getNet_value())).
                                divide(fundHistoryPojos.get(i).getNet_value()).doubleValue();
            }
            p.setHalfYearFallRise(halfYearFallRise);
            p.setHalfYearFallRisePer(nf.format(halfYearFallRise));
            //年涨跌幅度
            double  yearFallRise=0;
            for(int i=1;i<(fundHistoryPojos.size()<=365 ?fundHistoryPojos.size():365);i++){
                yearFallRise=yearFallRise+
                        (fundHistoryPojos.get(i-1).getNet_value().subtract(fundHistoryPojos.get(i).getNet_value())).
                                divide(fundHistoryPojos.get(i).getNet_value()).doubleValue();
            }
            p.setYearFallRise(yearFallRise);
            p.setYearFallRisePer(nf.format(yearFallRise));
        }





    }


    /**
     * 查询固定收益项目列表
     */
    public FixedIncomListData  queryFixedIncomeProduct (QueryFixIncomEvent event){

        //分页
        FixedIncomListData data =new FixedIncomListData();
        PageHelper.startPage(event.getPage(),event.getPageSize());
        List<PrivateProductInfo> privateProductInfos =  privateProductMapper.queryFixIncomeProduct();
        List<FixIncomListPojo> fixIncomListPojos =new ArrayList<>();
        for(PrivateProductInfo p :privateProductInfos){
            FixIncomListPojo f =new FixIncomListPojo();
            //查询出该项目的预计收益
            List<BigDecimal> expectProfitList =privateProductMapper.queryProductExceptProfit(p.getId());
            if(expectProfitList==null||expectProfitList.isEmpty())
            {
                f.setMaxExpectProfit("-");
                f.setMinExpectProfit("-");
            }else{
                f.setMaxExpectProfit(expectProfitList.stream().sorted((x,y)->
                        y.compareTo(x)).collect(Collectors.toList()).get(0).toString());
                f.setMinExpectProfit(expectProfitList.stream().sorted((x,y)->
                        x.compareTo(y)).collect(Collectors.toList()).get(0).toString());
            }
            //查询出该项目的最高预计收益
            f.setId(p.getId());
            f.setProduct_min_invest(p.getProduct_min_invest());
            f.setProduct_name(p.getProduct_name());
            f.setRaise_start_time(Format.format(p.getRaise_start_time()));
            f.setProduct_deadline(p.getProduct_deadline());
            f.setProduct_code(p.getProduct_code());
            fixIncomListPojos.add(f);
        }

        data.setList(fixIncomListPojos);
        data.setPage(event.getPage());
        data.setPageSize(event.getPageSize());
        long total =((Page) privateProductInfos).getTotal();
        data.setTotal(total);
        data.setTotalPage(NumberUtil.Division(event.getPageSize(),total));
        return  data;
    }

    /**
     * 查询固定收益详情
     */
    public  FixIncomeDetailPojo  queryFixedIncomeDetail (QueryDetailEvent event){

       FixIncomeDetailPojo fixIncomeDetailPojo =
                   privateProductMapper.queryFixIncomDetail(event.getProduct_id());
        return fixIncomeDetailPojo;
    }


    /**
     * 查询阳光私募,定向增发基金列表
     */
    public SunAndDirListData  querySunAndDirec(QuerySunAndDireEvent event){
        //分页
        SunAndDirListData data =new SunAndDirListData();
        PageHelper.startPage(event.getPage(),event.getPageSize());
        List<PrivateProductInfo> privateProductInfos =  privateProductMapper.querySunAndDire(event.getProduct_type());
        List<SunAndDirListPojo> sunAndDirListPojos =new ArrayList<>();
        for(PrivateProductInfo p :privateProductInfos){
            SunAndDirListPojo s =new SunAndDirListPojo();
            //查询出该项目的预计收益
            s.setId(p.getId());
            s.setProduct_min_invest(p.getProduct_min_invest());
            s.setProduct_name(p.getProduct_name());
            s.setProduct_code(p.getProduct_code());
            sunAndDirListPojos.add(s);
        }

        data.setList(sunAndDirListPojos);
        data.setPage(event.getPage());
        data.setPageSize(event.getPageSize());
        long total =((Page) privateProductInfos).getTotal();
        data.setTotal(total);
        data.setTotalPage(NumberUtil.Division(event.getPageSize(),total));
        return  data;
    }



    /**
     * 查询定向增发详情
     */
    public  PrPlaceDetailPojo  queryPrPlaceDetail (QueryDetailEvent event){

        PrPlaceDetailPojo prPlaceDetailPojo =
                privateProductMapper.queryPrPlaceDetail(event.getProduct_id());
        return prPlaceDetailPojo;
    }


    /**
     * 查询阳光私募详情
     */
    public SunProductPojo querySunProductDetail (QueryDetailEvent event){
        SunProductPojo sunProductPojo =
                privateProductMapper.querySunProductDetail(event.getProduct_id());
        List<SunNetValInfo> sunNetValInfos =sunNetValMapper.queryNetVal(event.getProduct_id());
        if(sunNetValInfos==null||sunNetValInfos.isEmpty()){
            sunProductPojo.setRaiseAndFall("---");
            sunProductPojo.setNet_val("---");
        }else {
            sunProductPojo.setNet_val(sunNetValInfos.get(0).getDwjz().toString());
            if(sunNetValInfos.size()<2){
                sunProductPojo.setNet_val("---");
            }else{
                Double yearFallRise = 0d;
                for(int i=1;i<(sunNetValInfos.size()<=365 ?sunNetValInfos.size():365);i++){
                    yearFallRise=yearFallRise+
                            (sunNetValInfos.get(i-1).getDwjz().subtract(sunNetValInfos.get(i).getDwjz())).
                                    divide(sunNetValInfos.get(i).getDwjz(),2,BigDecimal.ROUND_DOWN).doubleValue();
                }
                NumberFormat nf  =  NumberFormat.getPercentInstance();
                nf.setMinimumFractionDigits( 2 );
                sunProductPojo.setRaiseAndFall(nf.format(yearFallRise));
            }
        }
        return sunProductPojo;
    }

    /**
     * 查询首页推荐项目列表
     */
    public List queryHomeReProduct(){
        List<RecommendProductInfo> recommendProductInfos
                = recoProductMapper.queryReProduct(1);

        List homeProList =new ArrayList<>();
        for(RecommendProductInfo r :recommendProductInfos ){
            if(r.getType()==1){
                HSProductInfo hsProductInfo =hsProductMapper.queryHSProductByFundCode(r.getPro_code());
                List<FundNetDayInfo>  netDayList =fundNetDayMapper.getFundNetDay(r.getPro_code());
                HomeProductPojo h =new HomeProductPojo();
                h.setFund_name(hsProductInfo.getFund_name());
                h.setFund_code(hsProductInfo.getFund_code());
                h.setPer_myriad_income(hsProductInfo.getPer_myriad_income());
                h.setNet_value(hsProductInfo.getNet_value()) ;
                h.setFund_curr_ratio(hsProductInfo.getFund_curr_ratio());
                h.setStart_money(hsProductInfo.getStart_money());
                h.setFund_type(hsProductInfo.getOfundType());
                h.setProduct_type(1);
                if(!netDayList.isEmpty()) {
                    FundNetDayInfo netDayInfo =netDayList.get(0);
                    h.setDayFallRisePer(netDayInfo.getRzdf());
                }
                homeProList.add(h);

            }else {
                PrivateProductInfo priProInfo =privateProductMapper.queryPriProductByCode(r.getPro_code());
                    if(priProInfo.getProduct_type_id()==10027||priProInfo.getProduct_type_id()==10028){
                        //固定收益
                        HomeProductPojo h =new HomeProductPojo();
                        //查询出该项目的预计收益
                        List<BigDecimal> expectProfitList =privateProductMapper.queryProductExceptProfit(priProInfo.getId());
                        if(expectProfitList==null||expectProfitList.isEmpty())
                        {
                            h.setMaxExpectProfit("-");
                            h.setMinExpectProfit("-");
                        }else{
                            h.setMaxExpectProfit(expectProfitList.stream().sorted((x,y)->
                                    y.compareTo(x)).collect(Collectors.toList()).get(0).toString());
                            h.setMinExpectProfit(expectProfitList.stream().sorted((x,y)->
                                    x.compareTo(y)).collect(Collectors.toList()).get(0).toString());
                        }
                        //查询出该项目的最高预计收益
                        h.setFund_code(priProInfo.getProduct_code());
                        h.setProduct_id(priProInfo.getId());
                        h.setStart_money(priProInfo.getProduct_min_invest());
                        h.setFund_name(priProInfo.getProduct_name());
                        h.setRaise_start_time(Format.format(priProInfo.getRaise_start_time()));
                        h.setProduct_deadline(priProInfo.getProduct_deadline());
                        h.setProduct_type(2);
                        homeProList.add(h);
                    }else if(priProInfo.getProduct_type_id()==10029){  //阳光私募
                        HomeProductPojo h =new HomeProductPojo();
                        //查询出该项目的预计收益
                        h.setProduct_id(priProInfo.getId());
                        h.setFund_code(priProInfo.getProduct_code());
                        h.setStart_money(priProInfo.getProduct_min_invest());
                        h.setFund_name(priProInfo.getProduct_name());
                        h.setProduct_type(3);
                        homeProList.add(h);
                    }else if(priProInfo.getProduct_type_id()==10030){  //定向增发
                        HomeProductPojo h =new HomeProductPojo();
                        //查询出该项目的预计收益
                        h.setProduct_id(priProInfo.getId());
                        h.setFund_code(priProInfo.getProduct_code());
                        h.setStart_money(priProInfo.getProduct_min_invest());
                        h.setFund_name(priProInfo.getProduct_name());
                        h.setProduct_type(4);
                        homeProList.add(h);
                    }
            }

        }
        return  homeProList;

    }


    /**
     * 查询热销项目列表
     */
    public List queryHotProduct(){
        List<RecommendProductInfo> hotProductInfos
                = recoProductMapper.queryReProduct(2);
        List hotProList =new ArrayList<>();
        for(RecommendProductInfo r :hotProductInfos ){
                HSProductInfo hsProductInfo =hsProductMapper.queryHSProductByFundCode(r.getPro_code());
                HotProductPojo h =new HotProductPojo();
                h.setFund_name(hsProductInfo.getFund_name());
                h.setFund_code(hsProductInfo.getFund_code());
                hotProList.add(h);
        }
        return  hotProList;

    }

    public List allSearchProduct(){
        List<HSProductInfo> hsProductInfos =hsProductMapper.queryAvailableHSProductInfo();
        List hotProList =new ArrayList<>();
        for(HSProductInfo r :hsProductInfos ){
            HotProductPojo h =new HotProductPojo();
            h.setFund_name(r.getFund_name());
            h.setFund_code(r.getFund_code());
            hotProList.add(h);
        }
        return  hotProList;

    }


    /**
     * 关注的项目
     * @param event
     */
    public Msg  concernedProduct(ConcProductEvent event){
        if(concernedMapper.haveConcernedProduct(event.getFund_code(),event.getUser_tid())==1)
            return new Msg(Constant.FAIL,"该用户已关注过该项目");
        ConcernedProdcutInfo cp = concernedMapper.queryUserConcernedProduct(event.getFund_code(),event.getUser_tid());
        if(cp==null){
            concernedMapper.addConcernedProduct(event);  //如果没有关注记录 往数据库中插入记录
        }else{
           concernedMapper.concernedProduct(event.getFund_code(),event.getUser_tid());
        }
        Msg msg =new Msg();
        msg.setCode(Constant.SUCCESS);
        msg.setMsg(Constant.SUCCESS_MSG);
        return msg;
    }


    /**
     * 关注的公募项目List
     */
    public  List  queryPubConcernedList(Integer user_tid){
        List<ConcernedProdcutInfo> concernedProdcutInfos =
                concernedMapper.queryConCerenedProduct(1,user_tid);
        List<ConcerListPojo> listPojos =new ArrayList<>();
        for(ConcernedProdcutInfo c :concernedProdcutInfos){
            ConcerListPojo  l =new ConcerListPojo();
            HSProductInfo hsProductInfo =hsProductMapper.queryHSProductByFundCode(c.getPro_code());
            List<FundNetDayInfo>  netDayList =fundNetDayMapper.getFundNetDay(c.getPro_code());
            l.setFund_code(hsProductInfo.getFund_code());
            l.setFund_name(hsProductInfo.getFund_name());
            l.setPer_myriad_income(hsProductInfo.getPer_myriad_income());
            l.setNet_value(hsProductInfo.getNet_value()) ;
            l.setFund_curr_ratio(hsProductInfo.getFund_curr_ratio());
            l.setFund_type(hsProductInfo.getOfundType());
            l.setId(c.getId());
            if(!netDayList.isEmpty()) {
                FundNetDayInfo netDayInfo =netDayList.get(0);
                l.setDayFallRisePer(netDayInfo.getRzdf());
            }
            listPojos.add(l);
        }
        return  listPojos;
    }

    /**
     * 关注的私募项目List
     */
    public  List  queryPriConcernedList(Integer user_tid){
        List<ConcernedProdcutInfo> concernedProdcutInfos =
                concernedMapper.queryConCerenedProduct(2,user_tid);
        List<ConcerListPojo> listPojos =new ArrayList<>();
        for(ConcernedProdcutInfo c :concernedProdcutInfos){
            ConcerListPojo  l =new ConcerListPojo();
            PrivateProductInfo priProInfo =privateProductMapper.queryPriProductByCode(c.getPro_code());
            l.setFund_name(priProInfo.getProduct_name());
            if(priProInfo.getProduct_type_id()==10027||priProInfo.getProduct_type_id()==10028){
                l.setFund_type("固定收益");
            }else if(priProInfo.getProduct_type_id()==10029){
                l.setFund_type("阳光私募");
            }else if(priProInfo.getProduct_type_id()==10030){
                l.setFund_type("定向增发");
            }
            l.setId(c.getId());
            l.setFund_type_id(priProInfo.getProduct_type_id());
            listPojos.add(l);
        }
        return  listPojos;
    }


    /**
     * 移除关注的项目
     *
     */
    public void removeConcerned(String removeId){
        String[] removeArry= removeId.split(",",removeId.length()-1);
        concernedMapper.removeConcerned(removeArry);
    }


    /**
     * 是否有关注过该项目
     */
    public boolean  haveConcerned(HaveConcernedEvent event){
        if(concernedMapper.haveConcernedProduct(event.getFund_code(),event.getUser_tid())==1)
            return true;
        return false;
    }


    /**
     * 获取阳光私募基金 走势
     */
    public Msg getSunTrend(Integer product_id,Integer timelimit) throws Exception {
        Msg msg =new Msg();
        FundTrendData data =new FundTrendData();
        List<SunNetValInfo> infos =sunNetValMapper.queryNetVal(product_id);
        List<String>  dateList =new ArrayList<>();  //日期list
        List<String> netValueList=new ArrayList<>();//净值list
        if(infos.size()<timelimit)
            return  new Msg(Constant.FAIL,"无该基金走势信息");
        infos =infos.stream().limit(timelimit).collect(Collectors.toList());
        infos=infos.stream().sorted((o1,o2)->o1.getRiqi().compareTo(o2.getRiqi())).collect(Collectors.toList());
        for(SunNetValInfo s : infos){
            dateList.add(Format.format(s.getRiqi()));     //行情日期
            netValueList.add(s.getDwjz().toString());   //单位净值
        }
        data.setDateList(dateList);
        data.setNetValueList(netValueList);
        msg.setCode(Constant.SUCCESS);
        msg.setData(data);
        msg.setMsg(Constant.SUCCESS_MSG);
        return msg;
    }

}
