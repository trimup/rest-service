package com.lihe.persistence;

import com.lihe.entity.productDetail.FundAssetAllocationInfo;
import com.lihe.pojo.AssetAllocationPojo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by trimup on 2016/12/22.
 * 资产配置
 */
public interface FundAssetAllocationMapper {


    //根据基金代码 查询 资产配置
    @Select("select * from  lh_fund_asset_allocation where fund_code =#{fund_code} order by str_to_date(hqrq,'%Y-%m-%d') limit 1")
    FundAssetAllocationInfo queryAssetAllocationByCode(@Param("fund_code")String fund_code);


}
