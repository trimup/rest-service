package com.lihe.event.userBank;

import com.google.common.base.Strings;
import lombok.Data;

/**
 * Created by trimup on 2016/8/19.
 * 查询用户银行信息事件
 */
@Data
public class QueryUserBankEvent {

    private Integer user_tid;//用户id
    private String  token  ;//用户token

    public  boolean isCheck(){
        return user_tid==null|| Strings.isNullOrEmpty(token);
    }
}
