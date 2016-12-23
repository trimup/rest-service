/**
 * @Version 1.0.0
 * Copyright (c) 2016上海相诚金融-版权所有
 */
package com.lihe.replay.hsTrade;

import lombok.Data;

/**
 * @Class TradeApplyQueryApply
 * @Description 交易申请结果
 * @Author 张超超
 * @Date 2016/9/11 16:23
 */
@Data
public class TradeApplyQueryApply {
    private String code;
    private String message;
    private String details;
    private TradeApplyQueryPo[] tradeApplyQuerys;
    private Integer total_count;
    private Integer rowcount;
}
