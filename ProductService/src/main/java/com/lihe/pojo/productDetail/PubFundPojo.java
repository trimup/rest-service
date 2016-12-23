package com.lihe.pojo.productDetail;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by trimup on 2016/8/25.
 * 公募基金信息
 */
@Data
public class PubFundPojo {
    private Integer id;
    private String fund_full_name;//基金全称
    private String fund_code;//基金代码
    private String netValue ="---";//最新净值
    private String rzdf ="---" ;//日涨跌幅
    private BigDecimal startMoney;//起投额度
    private String  hqrq ="---";//净值日期
    private String buyRate ="---";//折后购买费率
    private String orgBuyRate ="---";// 原始购买费率
    private String yzdf ="---";//月涨跌幅
    private String jzdf ="---";//季度涨跌幅
    private String bnzdf="---";//半年涨跌幅
    private String nzdf="---";//年涨跌幅
    private Integer fund_status; //基金状态
    private String fundStatus;//基金状态

}
