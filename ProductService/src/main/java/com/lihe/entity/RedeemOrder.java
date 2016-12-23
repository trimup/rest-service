package com.lihe.entity;

import lombok.Data;

/**
 * Created by trimup on 2016/10/10.
 */
@Data
public class RedeemOrder {
   private String   id                    ;  //
   private String   user_tid              ;  //         '用户ID',
   private String   hs_product_tid        ;  //         '基金ID',
   private String   allot_no              ;  //        '申请编号',
   private String   fund_code             ;  //        '基金代码',
   private String   share_type            ;  //        '份额类型(A前收费 B后收费)',
   private String   trade_acco            ;  //        '交易账号',
   private String   shares                ;  //        '0.00' COMMENT '发生份额(赎回)',
   private String   apply_shares          ;  //        '0.00' COMMENT '申请份额(赎回中)',
   private String   apply_balance         ;  //        '0.00' COMMENT '申请金额(赎回中)',
   private String   create_time           ;  //        '创建时间',
   private String   apply_date            ;  //        '申请日期',
   private String   apply_time            ;  //        '申请时间',
   private String   clear_date            ;  //        '清算日期',
   private String   trade_confirm_balance ;  //        '交易确认金额',
   private String   trade_confirm_shares  ;  //        '交易确认份额',
   private String   fare_sx               ;  //        '手续费',
   private String   trade_status          ;  //        '交易处理状态',
   private String   trade_status_tid      ;  //        '交易状态',
   private String   affirm_date           ;  //        '确认日期',
   private String   comfirm_date          ;  //        '交易确认日期',
}
