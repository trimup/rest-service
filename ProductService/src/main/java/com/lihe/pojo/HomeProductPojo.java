package com.lihe.pojo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by trimup on 2016/9/2.
 * 主页项目
 */
@Data
public class HomeProductPojo {
    private String     dayFallRisePer       ; //日涨跌幅度
    private String     fund_name;//基金名称
    private String     fund_code ;//基金代码
    private BigDecimal     start_money;//起投金额
    private BigDecimal per_myriad_income; //万份收益
    private BigDecimal net_value ;          //产品当前净值
    private BigDecimal fund_curr_ratio;    //七日年化收益率
    private String    fund_type;//基金类型
    private  String   raise_start_time     ; //项目开始募资时间
    private String    maxExpectProfit   ; //最大的预计收益
    private String    minExpectProfit   ;//最小的预计收益
    private String    product_deadline         ;// varchar(20) NULL项目期限，按月为单位
    private Integer  product_id; //项目id
    private Integer  product_type;//项目类型  1.公募 2固定收益 3 定向增发和阳光私募
}
