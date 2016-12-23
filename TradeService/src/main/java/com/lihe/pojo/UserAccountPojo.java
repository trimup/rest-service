package com.lihe.pojo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by trimup on 2016/9/10.
 */
@Data
public class UserAccountPojo {
    private BigDecimal total_money;//账户总资产
    private String cumulative_profit_loss;//累计盈亏
    private String today_profit_loss;//当日盈亏
    private String date;//时间
}
