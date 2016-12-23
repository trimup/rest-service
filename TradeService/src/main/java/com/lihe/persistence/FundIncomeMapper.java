package com.lihe.persistence;

import com.lihe.entity.FundIncomeInfo;
import com.lihe.entity.UserIncomeInfo;
import org.apache.ibatis.annotations.Delete;
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

    @Insert("insert into lh_user_fund_income_history(fund_code,user_tid,income,income_date) " +
            " values(#{fund_code},#{user_tid},#{income},#{income_date})")
    public void insertFundIncomeHistory(FundIncomeInfo fundIncomeInfo);


    @Insert("insert into lh_user_income(user_tid,income,income_date) " +
            " values(#{user_tid},#{income},#{income_date}) ")
    public void insertUserEveryIncome(@Param("user_tid") Integer user_tid,
                                      @Param("income")BigDecimal income,@Param("income_date") String income_date);


    @Select("select * from  lh_user_income where user_tid =#{user_tid} order by create_time desc ")
    public List<UserIncomeInfo> queryUserIncome(@Param("user_tid")Integer user_tid);


    /**
     * 根据日期查询用户收益
     * @param user_tid
     * @param income_date
     * @return
     */
    @Select("select * from  lh_user_income where user_tid=#{user_tid} and income_date =#{income_date}")
    public UserIncomeInfo  queryUserIncomeByDate(@Param("user_tid")Integer user_tid,@Param("income_date")String income_date);



    @Select("select * from  lh_user_fund_income where " +
            " user_tid=#{user_tid} and fund_code =#{fund_code} order by create_time desc ")
    public List<FundIncomeInfo> queryFundIncomeByUser(
            @Param("fund_code")String fund_code,@Param("user_tid") Integer user_tid);


    /**
     * 获取该用户的累计盈亏
     * @param user_tid
     * @return
     */
    @Select("select if(sum(income)=null,sum(income),0) from lh_user_fund_income where user_tid =#{user_tid}")
    public BigDecimal getCumulativeProfit(@Param("user_tid")Integer user_tid);

    /**
     * 在用户没有持有该基金之后 删除用户对于该基金的收益
     */
    @Delete("delect from lh_user_fund_income where user_tid=#{user_tid} and fund_code=#{fund_code}")
    public void  delectFundIncomeOnUnhold(@Param("fund_code")String fund_code,@Param("user_tid") Integer user_tid);

}
