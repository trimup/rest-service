package com.lihe.persistence;

import com.lihe.entity.productDetail.FundRateinfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by trimup on 2016/8/25.
 */
public interface FundRateMapper {

    //查询出认购前端的费率
    @Select("select * from  lh_fund_rate_info where fund_code =#{fund_code} and rate_type =#{rate_type}")
    public List<FundRateinfo> queryFundRate(@Param("fund_code") String fund_code,@Param("rate_type") Integer rate_type);


}
