package com.lihe.Event;

import com.google.common.base.Strings;
import lombok.Data;

/**
 * Created by trimup on 2016/10/12.
 */
@Data
public class QueryOnlineEvalQuesEvent {
    private Integer user_tid;
    private String  token;

    public boolean orCheck(){
        return user_tid==null|| Strings.isNullOrEmpty(token);
    }
}
