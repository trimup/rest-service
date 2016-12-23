package com.lihe.pojo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by trimup on 2016/9/2.
 * 关注的项目列表
 */
@Data
public class ConcerListPojo {
    private Integer id;
    private String fund_code;
    private String fund_name;
    private String fund_type;
    private Integer fund_type_id;
    private BigDecimal per_myriad_income; //万份收益
    private BigDecimal net_value ;          //产品当前净值
    private BigDecimal fund_curr_ratio;    //七日年化收益率
    private String     dayFallRisePer       ; //日涨跌幅度
}
