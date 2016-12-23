package com.lihe.event.login;

import com.google.common.base.Strings;
import lombok.Data;

/**
 * Created by trimup on 2016/7/22.
 */
@Data
public class ResetUserPwdEvent {
    private String verifyCode;
    private String telephone;
    private String newPwd;

    public boolean isCheck(){
        return Strings.isNullOrEmpty(verifyCode)||
                Strings.isNullOrEmpty(telephone)||
                Strings.isNullOrEmpty(newPwd);
    }
}
