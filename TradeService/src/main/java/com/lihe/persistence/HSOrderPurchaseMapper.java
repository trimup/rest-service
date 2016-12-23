/**
 * @Version 1.0.0
 * Copyright (c) 2016上海相诚金融-版权所有
 */
package com.lihe.persistence;

import com.lihe.entity.HSOrderPurchase;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Class HSOrderPurchaseMapper
 * @Description
 * @Author 张超超
 * @Date 2016/9/5 18:36
 */
public interface HSOrderPurchaseMapper {

    @Insert({"INSERT INTO `lh_hs_order_purchase`(`user_tid`,`hs_product_tid`, `allot_no`, `trade_type`, `balance`, `fund_code`, ",
            "`share_type`, `trade_acco`, `trade_status_tid`, `trade_status`, `create_time`, `deduct_status`)",
            "VALUES(#{user_tid}, #{hs_product_tid}, #{allot_no}, #{trade_type}, #{balance}, #{fund_code}, #{share_type},",
                    "#{trade_acco}, #{trade_status_tid}, #{trade_status}, #{create_time}, #{deduct_status})"})
    @SelectKey(statement="select @@identity", keyProperty="id", before=false, resultType=int.class)
    int addHsOrderPurchase(HSOrderPurchase hsOrderPurchase);


    @Select({"SELECT * FROM `lh_hs_order_purchase` WHERE `id` = #{id}"})
    HSOrderPurchase findHsOrderPurchaseById(@Param("id") int id);

//    @Update({"UPDATE `lh_hs_order_purchase`",
//            "SET `apply_shares` = #{apply_shares}, `apply_balance` = #{apply_balance}",
//            "WHERE `id` = #{id}"})
//    int updateHsOrderPurchaseApplyData(@Param("id") int id, @Param("apply_shares") BigDecimal applyShares,
//                                       @Param("apply_balance") BigDecimal applyBalance);

    @Update({"UPDATE `lh_hs_order_purchase`",
            "SET `apply_shares` = #{apply_shares}, `apply_balance` = #{apply_balance}, `deduct_status` = #{deduct_status}",
            "WHERE `id` = #{id} AND `deduct_status` = #{deduct_status_old} AND `trade_status` = #{deduct_status}"})
    int updateHsOrderPurchaseApplyData(@Param("id") int id, @Param("apply_shares") BigDecimal applyShares,
                                       @Param("apply_balance") BigDecimal applyBalance,
                                       @Param("deduct_status") char deductStatus,
                                       @Param("deduct_status_old") char deductStatusOld,
                                       @Param("deduct_status") char trade_status);

    //处理付款失败的订单
    @Update({"UPDATE `lh_hs_order_purchase`",
            "SET `apply_shares` = #{apply_shares}, `apply_balance` = #{apply_balance}, `deduct_status` = #{deduct_status},",
            "`trade_status` = #{trade_status}, `trade_status_tid` = #{trade_status_tid}",
            "WHERE `id` = #{id} AND `deduct_status` = #{deduct_status_old} AND `trade_status` = #{trade_status_old}"})
    int updateHsOrderPurchaseApplyDataForFail(@Param("id") int id, @Param("apply_shares") BigDecimal applyShares,
                                       @Param("apply_balance") BigDecimal applyBalance,
                                       @Param("deduct_status") char deductStatus,
                                       @Param("deduct_status_old") char deductStatusOld,
                                       @Param("deduct_status") char trade_status,
                                       @Param("deduct_status_old") char trade_status_old,
                                       @Param("trade_status_tid") int trade_status_tid);

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
    @Update({"UPDATE `lh_hs_order_purchase`",
            "SET `trade_confirm_shares` = #{trade_confirm_shares},",
            "`trade_confirm_balance` = #{trade_confirm_balance},",
            "`trade_status` = #{trade_status}, `trade_status_tid` = #{trade_status_tid},",
            "`affirm_date` = #{affirm_date}, `comfirm_date` = #{comfirm_date}, ",
            "`fare_sx` = #{fare_sx}",
            "WHERE id = #{id}"})
    int updateHsOrderPurchaseTradeData(@Param("id") int id,
                                       @Param("trade_confirm_shares") BigDecimal trade_confirm_shares,
                                       @Param("trade_confirm_balance") BigDecimal trade_confirm_balance,
                                       @Param("trade_status") char trade_status,
                                       @Param("trade_status_tid") int trade_status_tid,
                                       @Param("affirm_date") int affirm_date,
                                       @Param("comfirm_date") Date comfirm_date,
                                       @Param("fare_sx") BigDecimal fare_sx);


    @Select({"<script>",
            "SELECT * FROM `lh_hs_order_purchase` WHERE 1=1 ",
            "<if test='trade_status_tid != null'>",
            "AND `trade_status_tid` = #{trade_status_tid}",
            "</if>",
            "<if test='list != null'>",
            "AND `trade_status_tid` IN ",
            "<foreach collection='list' item='item' index='index' open = '(' separator=',' close=')'>",
            "#{item}",
            "</foreach>",
            "</if>",
            "<if test='deduct_status != null'>",
            "AND `deduct_status` = #{deduct_status}",
            "</if>",
            "<if test='user_tid != null'>",
            "AND `user_tid` = #{user_tid}",
            "</if>",
            "</script>"})
    List<HSOrderPurchase> findHsOrderPurchaseList(Map<String, Object> paramMap);


    @Select("select distinct user_tid from lh_hs_order_purchase where deduct_status =3 ")
    List<Integer>  queryPurchaseUserTradeAcco();

}
