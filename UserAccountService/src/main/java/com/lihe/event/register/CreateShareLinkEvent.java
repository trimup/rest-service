package com.lihe.event.register;

import com.google.common.base.Strings;
import lombok.Data;

/**
 * Created by trimup on 2016/6/16.
 * 创建 加密分享链接 DES 加密
 *
 */
@Data
public class CreateShareLinkEvent {
    private Integer  user_tid; //推荐人ID
    private String   extend_type; //分享链接类型
    private Long     time; //截止时间
    private int      random;


    public boolean orEmpty(){
        return  user_tid==null|| Strings.isNullOrEmpty(extend_type)||time==null;
    }

}
