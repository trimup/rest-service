package com.lihe.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by trimup on 2016/9/5.
 */
@Data
public class HsOrderInfo {
    private Integer  id                 ;
    private String  user_id            ;           //用户ID
    private String  allot_no           ;          //申请编号
    private String  trade_type         ;     //  交易类型(0:申购;1:认购;2:赎回)
    private BigDecimal balance            ;       //发生金额(申购、认购)
    private String  fund_code          ;        //基金代码
    private String  share_type         ;         // 份额类型(A前收费 B后收费)
    private String  trade_acco         ;         //交易账号
    private String  shares             ;       //发生份额(赎回)
    private String  trade_status_tid   ;      //交易状态
    private String  apply_time         ;       // 申请时间
    private String  clear_date         ;      // 清算时间
}
