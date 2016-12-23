package com.lihe.event.login;

import com.google.common.base.Strings;
import lombok.Data;

/**
 * Created by trimup on 2016/8/17.
 */
@Data
public class CheckUserPwdEvent {
    private Integer  user_tid;
    private String   oldPwd;
    private String telephone;

    public boolean isCheck(){
        return user_tid==null|| Strings.isNullOrEmpty(oldPwd);
    }

}
