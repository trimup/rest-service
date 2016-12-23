package com.lihe.event.register;

import com.google.common.base.Strings;
import lombok.Data;

/**
 * Created by trimup on 2016/7/21.
 */
@Data
public class RegisterUserEvent {

    private String telephone;
    private String login_password ;//登录密码
    private String register_type        ; //   '注册渠道，1代表pc，2代表wechat，3代表ios，4代表android',
    private Integer   referrer_id =0 ;//推荐人的ID
    private String register_channel ="liheapp";//注册渠道
    private String faTelephone  ; //   专属理财师手机号
    private String verifyCode;

    public boolean  orEmpty(){
        return Strings.isNullOrEmpty(telephone)||Strings.isNullOrEmpty(login_password)||
                Strings.isNullOrEmpty(register_type)||Strings.isNullOrEmpty(verifyCode);
    }

}
