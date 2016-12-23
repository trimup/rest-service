package com.lihe.event.register;

import com.google.common.base.Strings;
import lombok.Data;

/**
 * Created by trimup on 2016/8/31.
 * 查询用户推荐人员
 */
@Data
public class QueryReUserEvent {
    private Integer  user_tid;//用户id
    private String   token  ;//

    public  boolean isCheck(){
        return user_tid==null|| Strings.isNullOrEmpty(token);
    }

}
