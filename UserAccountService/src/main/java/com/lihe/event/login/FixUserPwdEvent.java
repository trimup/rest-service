package com.lihe.event.login;

import com.google.common.base.Strings;
import lombok.Data;

/**
 * Created by trimup on 2016/7/22.
 */
@Data
public class FixUserPwdEvent {
    String telephone ;
    String oldPwd;
    String newPwd;


    public boolean isCheck(){
        return Strings.isNullOrEmpty(telephone)||
                Strings.isNullOrEmpty(oldPwd)||Strings.isNullOrEmpty(newPwd);
    }
}
