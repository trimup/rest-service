package com.lihe.event.hsTrade;

import com.google.common.base.Strings;
import lombok.Data;

/**
 * Created by trimup on 2016/10/21.
 * 查询申购结果
 */
@Data
public class QueryPurchaseResultEvent {
    private String allot_no;
    private String trade_acco;
    private Integer order_id;

    public boolean orCheck(){
        return Strings.isNullOrEmpty(allot_no)||
                Strings.isNullOrEmpty(trade_acco)||order_id==null;
    }
}
