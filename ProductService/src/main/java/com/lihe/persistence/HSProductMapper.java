package com.lihe.persistence;

import com.lihe.entity.HSProductInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by trimup on 2016/8/8.
 */
public interface HSProductMapper {


    @Select("select *,(select value_name from lh_hs_keyvalue_map where group_name='基金类别' and id =p.ofund_type) ofundType  from  lh_hs_product_info  p " +
            "where  is_view=1 and ofund_type= #{ofund_type} and fund_status in (1,0)  ")
    public List<HSProductInfo> queryHSProduct(@Param("ofund_type") String ofund_type);

    @Select("select *,(select  value_name from  lh_hs_keyvalue_map where group_name='基金状态' and id =p.fund_status ) fundStatus," +
            " (select  value_name from  lh_hs_keyvalue_map where group_name='基金类别' and id =p.ofund_type ) ofundType," +
            " (select  value_name from  lh_hs_keyvalue_map where group_name='基金风险等级' and id =p.ofund_risklevel ) ofundRisk" +
            "  from  lh_hs_product_info p where  is_view=1 and fund_code = #{fund_code}  ")
    public HSProductInfo queryHSProductByFundCode(@Param("fund_code") String fund_code);


    @Select("select * from  lh_hs_product_info_history where fund_code = #{fund_code} order by create_time desc ")
    public List<HSProductInfo> queryHSHistoryLog(@Param("fund_code") String fund_code);


    /**
     * 查询恒生的所有项目
     * @return
     */
    @Select("select * from  lh_hs_product_info ")
    public List<HSProductInfo>  queryAllHSProductInfo();

    /**
     * 查询恒生的所有项目
     * @return
     */
    @Select("select * from  lh_hs_product_info where is_view =1 and  fund_status in (1,0) and  ofund_type in ('2','A','E','B','F') ")
    public List<HSProductInfo>  queryAvailableHSProductInfo();
}
