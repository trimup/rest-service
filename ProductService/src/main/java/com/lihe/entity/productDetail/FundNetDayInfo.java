package com.lihe.entity.productDetail;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by trimup on 2016/8/25
 * 基金每日净值.
 */
@Data
public class FundNetDayInfo {
     private Integer  id           ;//  '主键ID',
     private String  fund_code    ;//  '基金代码',
     private String  create_time  ;//  '创建时间',
     private Date  update_time ;//更新时间
     private String         hqrq        ;   //  '行情日期',
     private Date  hqrqDate;//行情日期时间
     private String      dwjz        ;   //  '单位净值',
     private String      rzdf        ;   //  '日跌涨幅',
     private String      syl_z       ;   //  '近一周跌涨幅',
     private String      syl_y       ;   //  '近一个月跌涨幅',
     private String      syl_3y      ;   //  '近三个月跌涨幅',
     private String      syl_6y      ;   //  '近六个月跌涨幅',
     private String      syl_1n      ;   //  '近一年跌涨幅',
     private String      syl_2n      ;   //  '近两年跌涨幅',
     private String      syl_3n      ;   //  '近三年跌涨幅',
     private String      syl_jn      ;   //  '今年以来跌涨幅',
     private String      syl_ln      ;   //  '成立以来跌涨幅',
     private String stock_per;//股票占配比
     private String bank_per;//银行占配比
     private String bond_per ;// 债权占配比
     private String total_assets;//总资产



}
