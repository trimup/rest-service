package com.lihe.persistence;

import com.lihe.entity.productDetail.FundIncomeInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by trimup on 2016/9/22.
 */
public interface FundIncomeMapper {

    @Insert("insert into lh_user_fund_income(fund_code,user_tid,income,income_date) " +
            " values(#{fund_code},#{user_tid},#{income},#{income_date})")
    public void insertFundIncome(FundIncomeInfo fundIncomeInfo);


    @Select("select * from  lh_user_fund_income where " +
            " user_tid=#{user_tid} and fund_code =#{fund_code} order by create_time desc ")
    public List<FundIncomeInfo> queryFundIncomeByUser(
            @Param("fund_code")String fund_code,@Param("user_tid") Integer user_tid);


    /**
     * 用户基金的累计收益
     * @param fund_code
     * @param user_tid
     * @return
     */
    @Select("select if(sum(income)=null,sum(income),0) from  lh_user_fund_income where " +
            " user_tid=#{user_tid} and fund_code =#{fund_code}  ")
    public BigDecimal CumulativeProfitFundIncome(
            @Param("fund_code")String fund_code,@Param("user_tid") Integer user_tid);


    /**
     * 根据日期查询某只基金的收益
     * @param fund_code
     * @param user_tid
     * @param income_date
     * @return
     */
    @Select("select income from lh_user_fund_income where user_tid=#{user_tid} and fund_code =#{fund_code}")
    public BigDecimal queryFundIncomeByDate(@Param("fund_code")String fund_code,@Param("user_tid") Integer user_tid,
                                            @Param("income_date")String income_date);

}
