package com.lihe.Event;

import com.google.common.base.Strings;
import lombok.Data;

/**
 * Created by trimup on 2016/10/12.
 */
@Data
public class EvalResultEvent {
    private Integer user_tid;   //用户id
    private String  token ;    //用户token
    private String  elig_content ;    //用户提交答案

    public boolean isCheck(){
        return  user_tid==null|| Strings.isNullOrEmpty(token)||Strings.isNullOrEmpty(elig_content);
    }

}
