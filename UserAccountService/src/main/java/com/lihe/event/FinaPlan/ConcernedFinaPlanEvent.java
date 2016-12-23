package com.lihe.event.FinaPlan;

import com.google.common.base.Strings;
import lombok.Data;

/**
 * Created by trimup on 2016/9/27.
 * 关注理财顾问事件
 */
@Data
public class ConcernedFinaPlanEvent {
    private Integer user_tid; //用户id
    private String token ;
    private Integer fp_id;//理财师id

    public boolean orCheck(){
        return  user_tid==null|| Strings.isNullOrEmpty(token)||fp_id==null;
    }
}
