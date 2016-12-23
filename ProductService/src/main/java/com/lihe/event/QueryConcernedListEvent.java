package com.lihe.event;

import com.google.common.base.Strings;
import lombok.Data;

/**
 * Created by trimup on 2016/9/2.
 */
@Data
public class QueryConcernedListEvent {
    Integer type;  //1 公募 2私募
    private Integer user_tid;
    private String token;

    public boolean orCheck(){
        return  type ==null||
                Strings.isNullOrEmpty(token)||user_tid==null;
    }
 }
