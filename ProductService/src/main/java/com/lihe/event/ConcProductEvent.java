package com.lihe.event;

import com.google.common.base.Strings;
import lombok.Data;

/**
 * Created by trimup on 2016/9/2.
 * 关注项目时间
 */
@Data
public class ConcProductEvent {
    private Integer  user_tid;
    private String  token ;
    private String  fund_code ;
    private Integer  type;  //1公募 2私募


    public boolean  orCheck(){
        return  user_tid==null|| Strings.isNullOrEmpty(token)||
                Strings.isNullOrEmpty(fund_code)||type==null;

    }
}
