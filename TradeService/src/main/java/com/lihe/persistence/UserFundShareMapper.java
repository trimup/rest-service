package com.lihe.persistence;

import com.lihe.entity.UserFundShareInfo;
import com.lihe.replay.hsTrade.ShareQueryPo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by trimup on 2016/10/31.
 * 用户持有份额记录 （每日落地 计算收益）
 */
public interface UserFundShareMapper {
    @Insert("insert into  lh_user_fund_share(fund_code,user_tid,trade_acco,net_value,net_value_date," +
            "excetrans_in_total_quota,excetrans_out_total_quota,today_apply_total_quota,today_exceed_total_quota," +
            "today_transin_total_quota,today_transout_total_quota,unpaid_income,current_share,enable_shares,auto_buy,share_type,ta_acco,worth_value,pull_date) values(#{fund_code},#{user_tid}," +
            "#{trade_acco},#{net_value},#{net_value_date}," +
            "#{excetrans_in_total_quota},#{excetrans_out_total_quota},#{today_apply_total_quota},#{today_exceed_total_quota}," +
            "#{today_transin_total_quota},#{today_transout_total_quota},#{unpaid_income},#{current_share},#{enable_shares},#{auto_buy},#{share_type}," +
            "#{ta_acco},#{worth_value},curdate() )")
    public void insertUserFundShare(ShareQueryPo shareQueryPo, @Param("user_tid") Integer user_tid);


    @Insert("insert into  lh_user_fund_share_history(fund_code,user_tid,trade_acco,net_value,net_value_date," +
            "excetrans_in_total_quota,excetrans_out_total_quota,today_apply_total_quota,today_exceed_total_quota," +
            "today_transin_total_quota,today_transout_total_quota,unpaid_income,current_share,enable_shares,auto_buy,share_type,ta_acco,worth_value,pull_date) values(#{fund_code},#{user_tid}," +
            "#{trade_acco},#{net_value},#{net_value_date}," +
            "#{excetrans_in_total_quota},#{excetrans_out_total_quota},#{today_apply_total_quota},#{today_exceed_total_quota}," +
            "#{today_transin_total_quota},#{today_transout_total_quota},#{unpaid_income},#{current_share},#{enable_shares},#{auto_buy},#{share_type}," +
            "#{ta_acco},#{worth_value},curdate() )")
    public void insertUserFundShareHistroy(ShareQueryPo shareQueryPo, @Param("user_tid") Integer user_tid);



    @Select("select * from  lh_user_fund_share where pull_date=#{pull_date} ")
    public List<UserFundShareInfo> queryUserShareYesterday(@Param("pull_date")String pull_date);


    /**
     * 根据昨日的基金代码查询今日是否也拥有该基金
     * @return
     */
    @Select("select * from lh_user_fund_share where pull_date =#{pull_date} and user_tid=#{user_tid} and fund_code =#{fund_code}")
    public UserFundShareInfo queryUserShareByYesterdayRecord(@Param("pull_date")String pull_date,
                                                             @Param("user_tid")Integer user_tid,
                                                             @Param("fund_code")String fund_code);


    @Delete("delect from lh_user_fund_share where user_tid=#{user_tid} and fund_code=#{fund_code}")
    public void deleteUserFundShare(@Param("user_tid")Integer user_tid,@Param("fund_code")String fund_code);
}
