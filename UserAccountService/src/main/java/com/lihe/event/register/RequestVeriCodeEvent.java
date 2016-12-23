package com.lihe.event.register;

import lombok.Data;

/**
 * Created by trimup on 2016/7/21.
 * 请求获取验证码短信接口
 */
@Data
public class RequestVeriCodeEvent {
    private String telephone; //手机号码
    private Integer veri_type;// 验证码类型  1为 注册验证码 2忘记密码验证码 3修改安全密码验证码
}
