/**
 * @Version 1.0.0
 * Copyright (c) 2016上海相诚金融-版权所有
 */
package com.lihe.persistence;

import com.lihe.entity.HSUserFundInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Class HSUserFundInfoMapper
 * @Description
 * @Author 张超超
 * @Date 2016/9/9 13:50
 */
public interface HSUserFundInfoMapper {

    @Insert({"INSERT INTO `lh_hs_user_fund_info`(`hs_product_tid`,`user_tid`,`trade_acco`,`subscribed_shares`,",
            "`purchased_shares`,`redeemed_shares`,`enable_shares`,`frozen_shares`, `fund_code`, `auto_buy`)",
            "VALUES (#{hs_product_tid},#{user_tid},#{trade_acco},#{subscribed_shares},#{purchased_shares},",
                    "#{redeemed_shares},#{enable_shares},#{frozen_shares}, #{fund_code}, #{auto_buy})"})
    int addUserFundInfo(HSUserFundInfo userFundInfo);

    @Select({"SELECT * FROM `lh_hs_user_fund_info`",
            "WHERE 1 = 1",
            "AND `user_tid` = #{user_tid}",
            "AND `trade_acco` = #{trade_acco}",
            "AND `hs_product_tid` = #{hs_product_tid}"})
    List<HSUserFundInfo> findUserFundListByParams(@Param("user_tid") int userId, @Param("trade_acco") String tradeAcco,
                                                  @Param("hs_product_tid") int hsProductId);

    /**
     * 修改用户基金表数据（购买|赎回中、购买|赎回失败）
     * @param changeShares
     *          变动的份额
     * @param changeMoney
     *          变动的金额
     * @param id
     *          用户基金表ID
     * @param type
     *          操作类型
     * @return
     */
    @Update({"<script>",
            "UPDATE `lh_hs_user_fund_info` SET `user_tid` = `user_tid`",
            "<if test='type == 0'>",
            ",`purchased_shares` = `purchased_shares` + #{change_shares}",
            ",`purchased_money` = `purchased_money` + #{change_money}",
            "</if>",
            "<if test='type == 1'>",
            ",`purchased_shares` = `purchased_shares` - #{change_shares}",
            ",`purchased_money` = `purchased_money` - #{change_money}",
            "</if>",
            "<if test='type == 2'>",
            ",`subscribed_shares` = `subscribed_shares` + #{change_shares}",
            ",`subscribed_money` = `subscribed_money` + #{change_money}",
            "</if>",
            "<if test='type == 3'>",
            ",`subscribed_shares` = `subscribed_shares` - #{change_shares}",
            ",`subscribed_money` = `subscribed_money` - #{change_money}",
            "</if>",
            "<if test='type == 4'>",
            ",`frozen_shares` = `frozen_shares` + #{change_shares}",
            ",`enable_shares` = `enable_shares` - #{change_shares}",
            ",`frozen_money` = `frozen_money` + #{change_money}",
            "</if>",
            "<if test='type == 5'>",
            ",`frozen_shares` = `frozen_shares` - #{change_shares}",
            ",`enable_shares` = `enable_shares` + #{change_shares}",
            ",`frozen_money` = `frozen_money` - #{change_money}",
            "</if>",
            "WHERE `id` = #{id}",
            "</script>"})
    int updateHSUserFundInfo1(@Param("change_shares") BigDecimal changeShares,
                                   @Param("change_money") BigDecimal changeMoney,
                                   @Param("id") int id, @Param("type") int type);



    @Select("select * from lh_hs_user_fund_info where user_tid =#{user_tid} ")
    List<HSUserFundInfo>  queryLhUserFundById(@Param("user_tid")Integer user_tid);


    @Select("select * from lh_hs_user_fund_info where user_tid =#{user_tid} and fund_code =#{fund_code}")
    List<HSUserFundInfo>  queryUserFundByCode(@Param("user_tid")Integer user_tid,@Param("fund_code")String fund_code);

    /**
     * 修改用户基金表数据（购买|赎回成功、购买|赎回成功）
     * @param changeShares
     *          变动的份额
     * @param initialShares
     *          最初的份额
     * @param initialMoney
     *          最初的金额
     * @param id
     *          用户基金表ID
     * @param type
     *          操作类型
     * @return
     */
    @Update({"<script>",
            "UPDATE `lh_hs_user_fund_info` SET `user_tid` = `user_tid`",
            "<if test='type == 10'>",
            ",`purchased_shares` = `purchased_shares` - #{initial_shares}",
            ",`enable_shares` = `enable_shares` + #{change_shares}",
            ",`purchased_money` = `purchased_money` - #{initial_money}",
            "</if>",
            "<if test='type == 11'>",
            ",`subscribed_shares` = `subscribed_shares` - #{initial_shares}",
            ",`enable_shares` = `enable_shares` + #{change_shares}",
            ",`subscribed_money` = `subscribed_money` - #{initial_money}",
            "</if>",
            "<if test='type == 12'>",
            ",`frozen_shares` = `frozen_shares` - #{initial_shares}",
            ",`frozen_money` = `frozen_money` - #{initial_money}",
            "</if>",
            "WHERE `id` = #{id}",
            "</script>"})
    int updateHSUserFundInfo2(@Param("change_shares") BigDecimal changeShares,
                                           @Param("initial_shares") BigDecimal initialShares,
                                           @Param("initial_money") BigDecimal initialMoney,
                                           @Param("id") int id, @Param("type") int type);



    @Select("select * from lh_hs_user_fund_info  ")
    List<HSUserFundInfo>  queryAllLhUserFund();


    @Update("update lh_hs_user_fund_info set  getAuto_buy=#{getAuto_buy} where id =#{id}")
    void updateUserFundAutoType(@Param("getAuto_buy")String getAuto_buy,@Param("id")Integer id);

}
