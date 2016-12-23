package com.lihe.hundSunreply;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by trimup on 2016/10/14.
 * 交易成交返回
 */
@Data
public class TradeconfirmBean {
     private Integer   accept_time	        ; //   下单时间	N	6	0	N	v4.0.0.0
     private Integer   affirm_date	        ; //  确认日期	N	8	0	N	v4.0.0.0
     private String   allot_no	            ; //  申请编号	S	24	0	N	v4.0.0.0
     private String   apply_date	        ;     //  申请日期	N	8	0	N	v4.0.0.0
     private String   auto_buy	            ; //  分红方式	C	1	0	N	v4.0.0.0
     private String   bank_account	        ; //  银行账号	S	28	0	N	v4.0.0.0
     private String   capital_mode	        ; //  资金方式	C	2	0	N	v4.0.0.0
     private String   fail_cause	        ;     //  失败原因	S	255	0	N	v4.0.0.0
     private BigDecimal   fare_sx	            ; //   手续费	N	16	2	N	v4.0.0.0
     private String   fund_busin_code	    ; //  业务代码	S	3	0	N	v4.0.0.0
     private String   fund_code	            ; //  基金代码	S	6	0	N	v4.0.0.0
     private String   fund_froze_flag	    ; //  冻结方式	C	1	0	N	v4.0.0.0
     private String   fund_name	            ; //  基金名称	S	32	0	N	v4.0.0.0
     private String   money_type	        ;     //  币种类别	S	3	0	N	v4.0.0.0
     private BigDecimal net_value	            ; //  净值	N	11	4	N	v4.0.0.0
     private String   netno         	    ;     //  交易网点代码	S	9	0	N	v4.0.0.0
     private String   ofund_type	        ;     //  基金类型	C	1	0	N	v4.0.0.0
     private String   oppo_agency  	        ; //  对方销售商	S	3	0	N	v4.0.0.0
     private String   oppo_fund_account	    ; //  对方基金账号	S	12	0	N	v4.0.0.0
     private String   oppo_netno	        ;     //  对方销售网点编号	S	9	0	N	v4.0.0.0
     private String   original_appno	    ;     //  原申请单编号	S	24	0	N	v4.0.0.0
     private String   original_date	        ; //  下单日期	N	8	0	N	v4.0.0.0
     private String   return_code	        ; //  返回代码	S	8	0	N	v4.0.0.0
     private String   share_type	        ;     //  份额分类	C	1	0	N	v4.0.0.0
     private BigDecimal   stamptax	            ; //  印花税	N	16	0	N	v4.0.0.0
     private String   ta_acco	            ; //  TA账号	S	12	0	N	v4.0.0.0
     private BigDecimal   ta_fare	            ; //  ta收取的费用	N	16	2	N	v4.0.0.0
     private String   ta_serial_id	        ; //  TA确认编号	S	20	0	N	v4.0.0.0
     private String   target_fund_code	    ; //  目标基金代码	C	1	0	N	v4.0.0.0
     private String   target_fund_name	    ; //  目标基金名称	S	32	0	N	v4.0.0.0
     private String   target_share_type	    ; //  目标份额类型	C	1	0	N	v4.0.0.0
     private String   trade_acco	        ;     //  交易账号	S	17	0	N	v4.0.0.0
     private BigDecimal   trade_confirm_balance	; //  交易确认金额	N	16	2	N	v4.0.0.0
     private String   trade_confirm_type	;     //  交易确认份额
}
