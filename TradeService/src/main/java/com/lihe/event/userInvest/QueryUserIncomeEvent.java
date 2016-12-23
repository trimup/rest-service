package com.lihe.event.userInvest;

import com.google.common.base.Strings;
import lombok.Data;

/**
 * Created by trimup on 2016/9/27.
 * 查询用户收益事件
 */
@Data
public class QueryUserIncomeEvent {
    private Integer user_tid;
    private String  token ;
    private int page=1;
    private int pageSize=5;

    public boolean orCheck(){
        return  user_tid==null|| Strings.isNullOrEmpty(token);
    }
}
