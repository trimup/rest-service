package com.lihe.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by trimup on 2016/10/31.
 */
@Data
public class UserFundShareInfo {
    private Integer id;
    private String fund_code;
    private Integer user_tid;
    private String trade_acco;//交易帐号
    private BigDecimal net_value;//当日净值
    private String net_value_date;//净值日期
    private BigDecimal excetrans_in_total_quota;//当日赎回转购出总额
    private BigDecimal excetrans_out_total_quota;//当日赎回转购入总额
    private BigDecimal today_apply_total_quota;//当日申购总额
    private BigDecimal today_exceed_total_quota;//当日赎回总额
    private BigDecimal today_transin_total_quota;//当日转入总额
    private BigDecimal today_transout_total_quota;//当日转出总额
    private BigDecimal unpaid_income ;//未付收益金额
    private BigDecimal current_share;//当前份额
    private String  pull_date;//
}
