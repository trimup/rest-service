package com.lihe.persistence;

import com.lihe.entity.PrivateProductInfo;
import com.lihe.pojo.FixIncomeDetailPojo;
import com.lihe.pojo.PrPlaceDetailPojo;
import com.lihe.pojo.productDetail.SunProductPojo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by trimup on 2016/8/12.
 */
public interface PrivateProductMapper {

    /**
     * 查询固定收益项目列表
     * @return
     */
    @Select("select id,product_name,product_min_invest,raise_start_time,product_deadline from  lh_product_info where product_type_id in (10027,10028)  ")
    public List<PrivateProductInfo> queryFixIncomeProduct();

    /**
     * 查询固定收益项目列表
     * @return
     */
    @Select("select * from  lh_product_info where product_code =#{fund_code}  ")
    public PrivateProductInfo queryPriProductByCode(@Param("fund_code")String fund_code);


    /**
     * 查询固定收益的详情
     */
    @Select("select id,product_name,product_code,publish_agency,(select value_name from lh_keyvalue_map where value_id =p.product_status_id ) product_status," +
            "product_deadline,DATE_FORMAT(raise_start_time,'%Y-%m-%d') raise_start_time,product_min_invest," +
            " (select value_name from lh_keyvalue_map where value_id =p.pay_interest_type_id ) pay_interest_type ,collect_count,raise," +
            " (select value_name from lh_keyvalue_map where value_id =p.profit_type_id ) profit_type,raise_capital_account,fund_uses,payment_resource,rish_contro_step," +
            "product_excellence,memo   from  lh_product_info p  where  id =#{product_id}")
    public FixIncomeDetailPojo queryFixIncomDetail(Integer product_id);


    /**
     * 查询固定收益的详情
     */
    @Select("select id,product_name,product_code,publish_agency,(select value_name from lh_keyvalue_map where value_id =p.product_status_id ) product_status," +
            "product_min_invest,(select value_name from lh_keyvalue_map where value_id =p.pay_interest_type_id ) pay_interest_type " +
            ",raise,fund_uses,payment_resource," +
            "rish_contro_step,product_excellence,memo,DATE_FORMAT(project_forecast_time,'%Y-%m-%d') project_forecast_time," +
            " DATE_FORMAT(project_start_time,'%Y-%m-%d') project_start_time,product_management_fees,revenue_share,collect_count   " +
            " from   lh_product_info p  where  id =#{product_id}")
    public PrPlaceDetailPojo queryPrPlaceDetail(Integer product_id);



    /**
     * 查询阳光私募详情
     */
    @Select("select id,product_name,product_code,publish_agency,(select value_name from lh_keyvalue_map where value_id =p.product_status_id ) product_status," +
            " product_min_invest,DATE_FORMAT(project_forecast_time,'%Y-%m-%d') project_forecast_time,DATE_FORMAT(project_start_time,'%Y-%m-%d') project_start_time," +
            " product_management_fees,revenue_share,product_excellence,rish_contro_step,memo,fund_uses,payment_resource,raise " +
            " from   lh_product_info p  where  id =#{product_id}")
    public SunProductPojo querySunProductDetail(Integer product_id);


    /**
     * 查询阳光私募，定向增发项目列表
     * @return
     */
    @Select("select id,product_name,product_min_invest from  lh_product_info where product_type_id =#{product_type_id}  ")
    public List<PrivateProductInfo> querySunAndDire(@Param("product_type_id") Integer product_type_id);


    /**
     * 查询预计收益根据项目
     * @return
     */
    @Select("select expect_profit from lh_product_commision_ratio_info  where product_tid =#{product_tid} ")
    public List<BigDecimal> queryProductExceptProfit(@Param("product_tid") Integer product_tid);
}
