package com.lihe.pojo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by trimup on 2016/8/23.
 */
@Data
public class FundMarketPojo {
    private String  accept_hq_date	          ; //  接收行情日期	N	8	0	N	v4.0.0.0
    private String  forbid_modi_autobuy_flag  ;	// 禁止修改分红方式标志	C	1	0	N	v4.0.0.0
    private String  fund_code	              ; //  基金代码	S	6	0	N	v4.0.0.0
    private String  fund_contract_flag	      ; //  是否需要签署电子合同	C	1	0	N	v4.0.0.0		1-需要签署,空或者0-不需要签署
    private BigDecimal fund_curr_income	      ; //  基金收益	F	16	2	N	v4.0.0.0
    private BigDecimal  fund_curr_ratio	          ; //  基金收益率	F	10	5	N	v4.0.0.0
    private String  fund_full_name	          ; //  基金全称	S	100	0	N	v4.0.0.0
    private String  fund_name	              ; //  基金名称	S	40	0	N	v4.0.0.0
    private BigDecimal  fund_share	              ; //  基金总份额	F	16	2	N	v4.0.0.0
    private String  fund_status	              ; //  基金状态	C	1	0	N	v4.0.0.0
    private String  fund_sub_type	          ; //  基金子类型	C	1	0	N	v4.0.0.0
    private String  hq_date	                  ; //  行情日期	N	8	0	N	v4.0.0.0
    private BigDecimal  nav	                      ; //  T-1日基金单位净值	F	8	5	N	v4.0.0.0
    private BigDecimal  nav_total	              ; //  累计净值	F	7	4	N	v4.0.0.0
    private Integer  ofund_risklevel	          ; //  基金风险等级	C	1	0	N	v4.0.0.0
    private String  ofund_type	              ; //  基金类型	C	1	0	N	v4.0.0.0
    private BigDecimal  per_myriad_income	      ; //  万份单位收益	F	10	5	N	v4.0.0.0
    private String  pre_income_ratio	      ; //  预期年化收益率	F	10	5	N	v4.0.0.0
    private String  prod_term	              ; //  产品期限	S	64	0	N	v4.0.0.0
    private String  share_type	              ; //  份额分类	C	1	0	N	v4.0.0.0
    private String  ta_no	                  ; //  TA编号	S	4	0	N	v4.0.0.0     ;   //
}
