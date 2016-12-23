package com.lihe.event.priOrder;

import com.google.common.base.Strings;
import lombok.Data;

/**
 * Created by trimup on 2016/9/7.
 * 查询我预约的基金详情
 */
@Data
public class QueryMyOrderEvent {
    private Integer  user_tid;// 用户id
    private String   token ;//用户token
    private int page =1;
    private int pageSize=5;

    public boolean orCheck(){
        return  user_tid==null|| Strings.isNullOrEmpty(token);
    }
}
