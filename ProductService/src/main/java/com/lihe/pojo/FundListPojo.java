package com.lihe.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by trimup on 2016/8/12.
 */
@ApiModel("FundListPojo")
@Data
public class FundListPojo {
    @ApiModelProperty(required = true, value = "基金名称" )
    private String     fund_name;          //基金名称
    @ApiModelProperty(required = true, value = "基金代码" )
    private String     fund_code;         //基金代码
    private BigDecimal per_myriad_income; //万份收益
    private BigDecimal net_value ;          //产品当前净值
    private BigDecimal fund_curr_ratio;    //七日年化收益率
    private BigDecimal start_money;       //起购金额
    private String     dayFallRisePer       ; //日涨跌幅度
    private String     monthFallRisePer;    //月跌涨幅度
    private String     halfYearFallRisePer; //半年涨跌幅度
    private String     yearFallRisePer ;   //年跌涨幅度
    private String     quarterFallRisePer;  // 季度跌涨幅度
    private String     fund_type;  //基金类型
    private Integer   fund_status;//基金状态
    private String   fundStatus;//基金状态
}
