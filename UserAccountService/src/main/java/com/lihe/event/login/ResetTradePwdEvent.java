package com.lihe.event.login;

import com.google.common.base.Strings;
import lombok.Data;

/**
 * Created by trimup on 2016/8/4.
 */
@Data
public class ResetTradePwdEvent {
    private Integer  user_tid; //用户id
    private String   newPwd;  //新密码
    private String  token ;// token
    private String  identity_card;//身份证号码
    private String  verifyCode;



    public boolean isCheck(){
        return user_tid==null|| Strings.isNullOrEmpty(newPwd)||
                Strings.isNullOrEmpty(identity_card)||Strings.isNullOrEmpty(token)||Strings.isNullOrEmpty(verifyCode);
    }

}
