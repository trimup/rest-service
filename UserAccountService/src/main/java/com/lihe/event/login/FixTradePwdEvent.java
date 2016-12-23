package com.lihe.event.login;

import com.google.common.base.Strings;
import lombok.Data;

/**
 * Created by trimup on 2016/7/28.
 */
@Data
public class FixTradePwdEvent {
    private Integer  user_tid; //用户id
    private String  newPwd;  //新密码
    private String  oldPwd;  //旧密码
    private String  token ;// token


    public boolean isCheck(){
        return user_tid==null||Strings.isNullOrEmpty(newPwd)||
                Strings.isNullOrEmpty(oldPwd)||Strings.isNullOrEmpty(token);
    }
}
