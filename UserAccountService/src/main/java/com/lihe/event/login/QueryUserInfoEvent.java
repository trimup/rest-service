package com.lihe.event.login;

import com.google.common.base.Strings;
import lombok.Data;

/**
 * Created by trimup on 2016/8/17.
 * 获取用户信息event
 */
@Data
public class QueryUserInfoEvent {

    private Integer  user_tid;
    private String  token;

    public boolean isCheck(){
       return Strings.isNullOrEmpty(token)||user_tid==null;
    }
}
