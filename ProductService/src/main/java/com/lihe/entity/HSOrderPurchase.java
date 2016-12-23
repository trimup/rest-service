/**
 * @Version 1.0.0
 * Copyright (c) 2016上海相诚金融-版权所有
 */
package com.lihe.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Class HSOrderPurchase
 * @Description 公募交易订单表
 * @Author 张超超
 * @Date 2016/9/5 13:41
 */
@Data
public class HSOrderPurchase {
    private Integer id;
    private Integer user_tid; //用户ID
    private String allot_no; //申请编号
    private Integer hs_product_tid; //基金ID
    private Integer trade_type; //交易类型(0:申购;1:认购;2:赎回)
    private BigDecimal balance; //发生金额(申购、认购)
    private String fund_code; //基金代码
    private Character share_type; //份额类型(A前收费 B后收费)
    private String trade_acco; //交易账号
    private BigDecimal apply_shares; //申请份额(购买中)
    private BigDecimal apply_balance; //申请份额(购买中)
    private Integer trade_status_tid; //交易状态
    private Date create_time; //创建时间
    private Integer apply_date; //申请日期
    private Integer apply_time; //申请时间
    private Integer clear_date; //清算时间
    private BigDecimal trade_confirm_balance; //交易确认金额
    private BigDecimal trade_confirm_shares; //交易确认份额
    private BigDecimal fare_sx; //手续费
    private Character trade_status; //交易处理状态
    private Integer affirm_date; //确认日期
    private Date confirm_date; //确认日期
    private Integer  bonus_type;//分红类型
}
