/**
 * @Version 1.0.0
 * Copyright (c) 2016上海相诚金融-版权所有
 */
package com.lihe.event.hsUserFund;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Class HsUserFundForRedeemQueryEvent
 * @Description
 * @Author 张超超
 * @Date 2016/9/14 14:51
 */
@Data
public class HsUserFundForRedeemQueryEvent {
    private Integer id = -1;
    private String productName = ""; //基金名称
    private BigDecimal totalAssets = new BigDecimal("0"); //总资产
    private BigDecimal totalShares = new BigDecimal("0"); //总份额
    private BigDecimal enableShares = new BigDecimal("0"); //可赎回份额
    private String expectedDateStr = ""; //预计赎回日期
    private String expectedDateStrFormart = ""; //预计赎回日期（格式化文字）
}
