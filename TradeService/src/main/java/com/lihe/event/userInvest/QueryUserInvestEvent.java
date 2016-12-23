package com.lihe.event.userInvest;

import com.google.common.base.Strings;
import lombok.Data;

/**
 * Created by trimup on 2016/9/9.
 */
@Data
public class QueryUserInvestEvent {
    private Integer user_tid;
    private String  token;
    private Integer  sort_type=0;  //0 全部 1.待确认 2.已持仓 3.赎回中 4,已赎回
    private int page =1;
    private int pageSize=5;

    public boolean orCheck(){
        return  user_tid==null||Strings.isNullOrEmpty(token)||sort_type==null;
    }
}
