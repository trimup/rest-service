package com.lihe.event;

import com.google.common.base.Strings;
import lombok.Data;

/**
 * Created by trimup on 2016/8/26.
 * 查询基金趋势事件
 */
@Data
public class QueryFundTrendEvent {
    private String  fund_code;
    private Integer time_limit;

    public boolean isCheck(){
        return  Strings.isNullOrEmpty(fund_code)||time_limit==null;
    }
}
