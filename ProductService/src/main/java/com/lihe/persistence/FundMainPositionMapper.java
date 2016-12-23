package com.lihe.persistence;

import com.lihe.entity.productDetail.FundMainPositionsInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by trimup on 2016/8/26.
 */
public interface FundMainPositionMapper {

    @Select("select * from lh_fund_main_positions where fund_code =#{fund_code} " +
            "  and name is not null and LENGTH(trim(name))>0  " +
            "  and code is not null and LENGTH(trim(code))>0 " +
            "  and capital is not null and LENGTH(trim(capital))>0 " +
            "  and accounting_ratio is not null and LENGTH(trim(accounting_ratio))>0 ")
    public List<FundMainPositionsInfo>  queryFundMainPostion(@Param("fund_code") String fund_code);
}
