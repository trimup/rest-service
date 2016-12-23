package com.lihe.persistence;

import com.lihe.entity.PrivateProductInfo;
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
    public PrivateProductInfo queryPriProductByCode(@Param("fund_code") String fund_code);


    /**
     * 查询固定收益项目列表
     * @return
     */
    @Select("select count(1) from  lh_product_info where product_code =#{fund_code}  ")
    public Integer countPriProductByCode(@Param("fund_code") String fund_code);



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



    /**
     * 查询所有私募项目
     * @return
     */
    @Select("select * from  lh_product_info  ")
    public List<PrivateProductInfo> queryAllPriProduct();
}
