/**
 * @Version 1.0.0
 * Copyright (c) 2016上海相诚金融-版权所有
 */
package com.lihe.persistence;

import com.lihe.entity.HSOrderRedeem;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Class HSOrderRedeemMapper
 * @Description
 * @Author 张超超
 * @Date 2016/9/12 17:12
 */
public interface HSOrderRedeemMapper {
    @Insert({"INSERT INTO `lh_hs_order_redeem`(`user_tid`, `hs_product_tid`, `allot_no`, `fund_code`, `share_type`, ",
            "`trade_acco`, `shares`, `trade_status_tid`, `trade_status`, `create_time`)",
            "VALUES(#{user_tid}, #{hs_product_tid}, #{allot_no}, #{fund_code}, #{share_type},",
            "#{trade_acco}, #{shares}, #{trade_status_tid}, #{trade_status}, #{create_time})"})
    @SelectKey(statement="select @@identity", keyProperty="id", before=false, resultType=int.class)
    int addHsOrderRedeem(HSOrderRedeem hsOrderRedeem);


    @Select({"SELECT * FROM `lh_hs_order_redeem` WHERE `id` = #{id}"})
    HSOrderRedeem findHsOrderRedeemById(@Param("id") int id);

    @Update({"UPDATE `lh_hs_order_redeem`",
            "SET `apply_shares` = #{apply_shares}, `apply_balance` = #{apply_balance}",
            "WHERE id = #{id}"})
    int updateHsOrderRedeemApplyData(@Param("id") int id, @Param("apply_shares") BigDecimal applyShares,
                                       @Param("apply_balance") BigDecimal applyBalance);

    /**
     * 交易查询结果修改
     * @param id
     *          id
     * @param trade_confirm_shares
     *          确认份额
     * @param trade_confirm_balance
     *          确认金额
     * @param trade_status
     *          交易状态
     * @param trade_status_tid
     *          交易状态
     * @param affirm_date
     *          确认时间
     * @param comfirm_date
     *          确认时间
     * @return
     */
    @Update({"UPDATE `lh_hs_order_redeem`",
            "SET `trade_confirm_shares` = #{trade_confirm_shares},",
            "`trade_confirm_balance` = #{trade_confirm_balance},",
            "`trade_status` = #{trade_status}, `trade_status_tid` = #{trade_status_tid},",
            "`affirm_date` = #{affirm_date}, `comfirm_date` = #{comfirm_date}, ",
            "`fare_sx` = #{fare_sx}",
            "WHERE id = #{id}"})
    int updateHsOrderRedeemTradeData(@Param("id") int id,
                                       @Param("trade_confirm_shares") BigDecimal trade_confirm_shares,
                                       @Param("trade_confirm_balance") BigDecimal trade_confirm_balance,
                                       @Param("trade_status") Character trade_status,
                                       @Param("trade_status_tid") int trade_status_tid,
                                       @Param("affirm_date") Integer affirm_date,
                                       @Param("comfirm_date") Date comfirm_date,
                                       @Param("fare_sx") BigDecimal fare_sx);


    @Select({"<script>",
            "SELECT * FROM `lh_hs_order_redeem` WHERE 1=1 ",
            "<if test='trade_status_tid != null'>",
            "AND `trade_status_tid` = #{trade_status_tid}",
            "</if>",
            "<if test='user_tid != null'>",
            "AND `user_tid` = #{user_tid}",
            "</if>",
            "</script>"})
    List<HSOrderRedeem> findHsOrderRedeemList(Map<String, Object> paramMap);
}
