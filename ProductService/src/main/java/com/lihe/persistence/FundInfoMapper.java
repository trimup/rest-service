package com.lihe.persistence;

import com.lihe.entity.productDetail.FundInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by trimup on 2016/8/25.
 */
public interface FundInfoMapper {


    /**
     * 根据基金代码查询基金信息
     * @param fund_code
     * @return
     */
    @Select("select * from lh_fund_info where fund_code =#{fund_code}")
    public FundInfo queryFundByCode(@Param("fund_code")String fund_code);
}
