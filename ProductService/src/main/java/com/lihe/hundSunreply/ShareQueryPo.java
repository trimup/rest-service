package com.lihe.hundSunreply;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by trimup on 2016/10/14.
 */
@Data
public class ShareQueryPo {
    private BigDecimal accum_income    	          ;  //累计收益	N	16	2	N	v4.0.0.0
    private String   auto_buy	                  ;  //分红方式	C	1	0	N	v4.0.0.0
    private BigDecimal   available_due_share	          ;  //可用到期份额	N	16	2	N	v4.0.0.0
    private String   bank_account	              ;  //银行账号	S	32	0	N	v4.0.0.0
    private String   bank_no	                      ;  //银行代码	S	3	0	N	v4.0.0.0
    private BigDecimal   business_frozen_amount	      ;  //交易冻结数量	N	10	0	N	v4.0.0.0
    private String   capital_mode	              ;  //资金方式	S	2	0	N	v4.0.0.0
    private BigDecimal   current_share	              ;  // 当前份额	N	16	2	N	v4.0.0.0
    private BigDecimal   enable_shares	              ;  // 可用份额	N	16	2	N	v4.0.0.0
    private BigDecimal   excetrans_in_total_quota	  ;  //当日赎回转购出总额	N	16	2	N	v4.0.0.0
    private BigDecimal   excetrans_out_total_quota	  ;  //当日赎回转购入总额	N	16	2	N	v4.0.0.0
    private BigDecimal   frozen_share	              ;  //基金冻结数量	N	16	2	N	v4.0.0.0
    private String   fund_code	                  ;  //基金代码	S	6	0	N	v4.0.0.0
    private String   fund_name	                  ;  //基金名称	S	32	0	N	v4.0.0.0
    private String   last_nav	                  ;  //上一日净值	F	16	2	N	v4.0.0.0
    private BigDecimal   net_value	                  ;  //单位净值	F	16	2	N	V4.0.0.0
    private String   net_value_date	              ;  //净值日期	N	8	0	N	v4.0.0.0
    private BigDecimal   quick_exceed_enable_share	  ;  //快速赎回可用份额	N	16	2	N	v4.0.0.0
    private String   share_type	                  ;  //份额分类	C	1	0	N	v4.0.0.0
    private String   ta_acco	                      ;  //TA账号/基金账号	S	12	0	N	v4.0.0.0
    private BigDecimal   today_apply_total_quota	      ;  //当日申购总额	N	16	2	N	v4.0.0.0
    private BigDecimal   today_exceed_total_quota	  ;  //当日赎回总额	N	16	2	N	v4.0.0.0
    private BigDecimal   today_income	              ;  //每日收益	N	16	2	N	v4.0.0.0
    private BigDecimal   today_transin_total_quota	  ;  //当日转入总额	N	16	2	N	v4.0.0.0
    private BigDecimal   today_transout_total_quota	  ;  //当日转出总额	N	16	2	N	v4.0.0.0
    private String   trade_acco	                  ;  //交易账号	S	17	0	N	v4.0.0.0
    private BigDecimal   unpaid_income	              ;  //未付收益金额	N	16	2	N	v4.0.0.0
    private BigDecimal   worth_value	                  ;  //市值
}
