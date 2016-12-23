package com.lihe.event.register;

import com.google.common.base.Strings;
import lombok.Data;

/**
 * Created by trimup on 2016/6/16.
 *
 * 分享注册新用户
 */
@Data
public class ShareReUserEvent {
    private String telephone;
    private String login_password ;//登录密码
    private String register_type;//注册来源
    private String register_channel ="liheapp";//注册渠道
    private String verifyCode;
    private String share_link ; //分享链接
//    private String enCode1;//MD5加密后的图片验证码
//    private String enCode2;//AES加密后的图形验证码


    public boolean orEmpty(){
        return Strings.isNullOrEmpty(telephone)||Strings.isNullOrEmpty(login_password)||Strings.isNullOrEmpty(register_type)
                ||Strings.isNullOrEmpty(register_channel)||Strings.isNullOrEmpty(verifyCode)
                ||Strings.isNullOrEmpty(share_link);
    }
}
