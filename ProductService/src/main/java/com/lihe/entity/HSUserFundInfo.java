/**
 * @Version 1.0.0
 * Copyright (c) 2016上海相诚金融-版权所有
 */
package com.lihe.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Class HSUserFundInfo
 * @Description 用户基金信息
 * @Author 张超超
 * @Date 2016/9/9 13:41
 */
@Data
public class HSUserFundInfo {
    private Integer id;
    private Integer hs_product_tid; //公募基金ID
    private Integer user_tid; //用户ID
    private String trade_acco; //交易账号
    private String fund_code ;// 基金代码
    private BigDecimal subscribed_shares = new BigDecimal("0"); //认购份额
    private BigDecimal purchased_shares = new BigDecimal("0"); //申购份额
    private BigDecimal redeemed_shares = new BigDecimal("0"); //赎回份额
    private BigDecimal enable_shares = new BigDecimal("0"); //可用份额
    private BigDecimal frozen_shares = new BigDecimal("0"); //冻结份额
    private BigDecimal subscribed_money = new BigDecimal("0"); //认购金额
    private BigDecimal purchased_money = new BigDecimal("0"); //申购金额
    private BigDecimal frozen_money = new BigDecimal("0"); //冻结金额
    private BigDecimal redeemed_money = new BigDecimal("0"); //赎回金额
    private Integer  auto_buy;
}
