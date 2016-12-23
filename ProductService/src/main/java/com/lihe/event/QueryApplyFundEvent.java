package com.lihe.event;

import com.google.common.base.Strings;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by trimup on 2016/10/14.
 */
@Data
public class QueryApplyFundEvent {
    Integer user_tid;//用户id
    String token ;// 用户token
    String fund_code ;//基金代码
    String  allot_no;//申请编号

    public boolean isCheck(){
        return user_tid==null||
                Strings.isNullOrEmpty(token)||Strings.isNullOrEmpty(fund_code)||Strings.isNullOrEmpty(allot_no);
    }
}
