package com.lihe.persistence;

import com.lihe.entity.productDetail.FundNetDayInfo;
import com.lihe.pojo.productDetail.FundNetDayPojo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by trimup on 2016/8/25.
 */
public interface FundNetDayMapper {


    /**
     * 获取基金历史净值
     * @param fund_code
     * @return
     */
    @Select("select *,str_to_date(hqrq,'%Y-%m-%d') hqrqDate from  lh_fund_net_day where fund_code =#{fund_code} and hqrq is not null and dwjz is not null " +
            "order by str_to_date(hqrq,'%Y-%m-%d') desc")
    public List<FundNetDayInfo> getFundNetDay(@Param("fund_code") String fund_code);


    @Insert("insert into `lh_fund_net_day` " +
            "( `fund_code`, `create_time`, `update_time`, `hqrq`, `dwjz`, `rzdf`, `syl_z`, `syl_y`," +
            " `syl_3y`, `syl_6y`, `syl_1n`, `syl_2n`, `syl_3n`, `syl_jn`, `syl_ln`, " +
            "`stock_per`, `bank_per`, `bond_per`, `total_assets`) " +
            "values(#{fund_code},'2016-08-29 10:25:29',NULL,#{date},'2.12','1.3%','1.2%','3.2%','4.3%','2.5%','2.3%','1.4%','2.5%','6.1%','2.2%','20%','30%','10%','6.7千万')")
    public void insertRateValue(@Param("date") String date,@Param("fund_code")String fund_code);


    @Insert("insert into `lh_jz` (product_id,riqi,ljsy,dwjz) values (#{product_id},#{date},'1.1','1.3') ")
    public void insertValue(@Param("date") String date,@Param("product_id")String product_id);
}
