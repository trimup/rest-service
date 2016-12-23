package com.lihe.pojo.productDetail;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by trimup on 2016/8/25.
 * 基金每日净值
 */
@Data
public class FundNetDayPojo {
    String hqrq;     //行情日期
    String dwjz; // 单位净值
    String rzdf; // 日涨跌幅
}
