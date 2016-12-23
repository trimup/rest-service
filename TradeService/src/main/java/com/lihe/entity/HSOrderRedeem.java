/**
 * @Version 1.0.0
 * Copyright (c) 2016上海相诚金融-版权所有
 */
package com.lihe.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Class HSOrderRedeem
 * @Description 利和公募赎回订单表
 * @Author 张超超
 * @Date 2016/9/11 11:25
 */
@Data
public class HSOrderRedeem {
     private Integer id; //ID
     private Integer user_tid; //用户ID
     private Integer hs_product_tid; //公募基金ID
     private String allot_no; //申请编号
     private String fund_code; //基金代码
     private Character share_type; //份额类型(A前收费 B后收费)
     private Character deduct_status; //扣款状态（0:未校验（用户线下付款时）; 1:无效; 2:有效; 3:已发送扣款指令）
     private String trade_acco; //交易账号
     private BigDecimal shares; //发生份额(赎回)
     private BigDecimal apply_shares; //申请份额
     private BigDecimal apply_balance; //申请金额
     private Integer trade_status_tid; //交易状态
     private Date create_time; //创建时间
     private Integer apply_date; //申请日期
     private Integer apply_time; //申请时间
     private Integer clear_date; //清算日期
     private BigDecimal trade_confirm_balance; //交易确认金额
     private BigDecimal trade_confirm_shares; //交易确认份额
     private BigDecimal fare_sx; //手续费
     private Character trade_status; //交易处理状态
     private Integer affirm_date; //确认日期
     private Date confirm_date; //确认日期
}
