package com.lihe.pojo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by trimup on 2016/8/9.
 */
@Data
public class HSListPojo {
    private String fund_name;//基金名称
    private String fund_code;//基金代码
    private BigDecimal per_myriad_income;//万份收益
    private BigDecimal net_value ;//产品当前净值
    private BigDecimal fund_curr_ratio;//七日年化收益率
    private BigDecimal start_money;//起购金额
    private String  fundType;//基金类型
    private String dayFallRisePer ="---"       ; //日涨跌幅度
    private Double dayFallRise =0d;
    private String monthFallRisePer="---";//月跌涨幅度
    private Double monthFallRise=0d;
    private String halfYearFallRisePer="---";//半年涨跌幅度
    private Double halfYearFallRise=0d;
    private String yearFallRisePer="---" ; //年跌涨幅度
    private Double yearFallRise=0d;
    private String quarterFallRisePer="---";// 季度跌涨幅度
    private Double quarterFallRise=0d;
}
