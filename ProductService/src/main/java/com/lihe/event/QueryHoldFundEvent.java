package com.lihe.event;

import com.google.common.base.Strings;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by trimup on 2016/9/22.
 */
@Data
public class QueryHoldFundEvent {
    Integer user_tid;//用户id
    String token ;// 用户token
    String fund_code ;//基金代码

    public boolean isCheck(){
       return user_tid==null||
               Strings.isNullOrEmpty(token)||Strings.isNullOrEmpty(fund_code);
    }
}
