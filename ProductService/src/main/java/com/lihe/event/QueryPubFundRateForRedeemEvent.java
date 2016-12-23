/**
 * @Version 1.0.0
 * Copyright (c) 2016上海相诚金融-版权所有
 */
package com.lihe.event;

import com.google.common.base.Strings;
import lombok.Data;

/**
 * @Class QueryPubFundRateForRedeemEvent
 * @Description
 * @Author 张超超
 * @Date 2016/9/22 16:20
 */
@Data
public class QueryPubFundRateForRedeemEvent {
    private String fund_code;

    public boolean isCheck(){
        return Strings.isNullOrEmpty(fund_code);
    }
}
