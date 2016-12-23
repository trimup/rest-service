/**
 * @Version 1.0.0
 * Copyright (c) 2016上海相诚金融-版权所有
 */
package com.lihe.HundSunReply;

import lombok.Data;

/**
 * @Class TradeResultQueryApply
 * @Description
 * @Author 张超超
 * @Date 2016/9/8 15:29
 */
@Data
public class TradeResultQueryApply {
    private String code;
    private String message;
    private String details;
    private TradeResultQueryPo[] tradeResultQuerys;
    private Integer total_count;
    private Integer rowcount;
}
