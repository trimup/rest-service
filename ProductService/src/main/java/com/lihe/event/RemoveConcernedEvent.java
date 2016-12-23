package com.lihe.event;

import com.google.common.base.Strings;
import lombok.Data;

import java.util.List;

/**
 * Created by trimup on 2016/9/2.
 * 移除 关注的事件
 */
@Data
public class RemoveConcernedEvent {
    private Integer user_tid;
    private String token ;
    private String removeId ;

    public boolean orCheck(){
        return user_tid==null||
                Strings.isNullOrEmpty(token)||Strings.isNullOrEmpty(removeId);
    }
}
