package com.lihe.replay.userInvest;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by trimup on 2016/9/21.
 * 浮动盈亏返回
 */
@Data
public class FloatingProfitReply {
    private String   code	;
	private BigDecimal   buy_cost ;
    private BigDecimal gain_balance ;
    private String   message       ;
    private BigDecimal   worth_value	;
}
