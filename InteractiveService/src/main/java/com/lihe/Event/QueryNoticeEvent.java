package com.lihe.Event;

import com.google.common.base.Strings;
import lombok.Data;

/**
 * Created by trimup on 2016/9/13.
 */
@Data
public class QueryNoticeEvent {
    private Integer type;  //类型1项目公告2系统消息3活动消息
    private Integer user_tid;
    private String token ;
    private int page = 1;
    private int pageSize = 5;
    public boolean isCheck(){
        return  type==null||user_tid==null|| Strings.isNullOrEmpty(token);

    }
}
