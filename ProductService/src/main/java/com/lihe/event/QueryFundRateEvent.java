package com.lihe.event;

import com.google.common.base.Strings;
import lombok.Data;

/**
 * Created by trimup on 2016/8/30.
 * 查询基金费率事件
 */
@Data
public class QueryFundRateEvent {
    private String  fund_code ;//基金代码

    public boolean isCheck(){
       return Strings.isNullOrEmpty(fund_code);
    }
}
