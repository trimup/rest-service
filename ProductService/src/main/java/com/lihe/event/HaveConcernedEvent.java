package com.lihe.event;

import com.google.common.base.Strings;
import lombok.Data;

/**
 * Created by trimup on 2016/9/2.
 * 是否有关注该项目事件
 */
@Data
public class HaveConcernedEvent {
    private Integer user_tid;
    private String  token;
    private String  fund_code;


    public boolean isCheck(){
        return user_tid==null|| Strings.isNullOrEmpty(token)||Strings.isNullOrEmpty(fund_code);
    }
}
