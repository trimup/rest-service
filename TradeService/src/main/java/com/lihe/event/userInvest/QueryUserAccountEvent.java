package com.lihe.event.userInvest;

import com.google.common.base.Strings;
import lombok.Data;

/**
 * Created by trimup on 2016/9/26.
 */
@Data
public class QueryUserAccountEvent {
    private Integer user_tid;
    private String token ;
    public boolean orCheck(){
        return user_tid==null|| Strings.isNullOrEmpty(token);
    }
}
