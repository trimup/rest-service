package com.lihe.pojo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by trimup on 2016/8/11.
 */
@Data
public class FundHistoryPojo {
    private BigDecimal fund_curr_ratio	;  //      七日年化收益率	F	9	4	N	v4.0.0.0
    private String    fund_income	    ;  //      基金收益	F	16	2	N	v4.0.0.0
    private String    fund_name	        ;  //  基金名称	S	20	0	N	v4.0.0.0
    private String    fund_status	    ;  //      基金状态	C	1	0	N	v4.0.0.0		字典[基金状态]
    private String    fund_total_share	;  //  基金总份额	F	16	2	N	v4.0.0.0
    private Long    nav_date	        ;  //  净值日期	N	8	0	N	v4.0.0.0
    private String    nav_total	        ;  //  累计净值	F	7	4	N	v4.0.0.0
    private double   net_value	        ;  //  基金净值	N	8	4	N	v4.0.0.0
    private String   per_myriad_income	;  //  万份基金单位收益	F	9	4	N	v4.0.0.0
    private String   share_type         ;  //  份额类别
}
