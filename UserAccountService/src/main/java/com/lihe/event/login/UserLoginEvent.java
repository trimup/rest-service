package com.lihe.event.login;

import lombok.Data;

/**
 * Created by trimup on 2016/7/22.
 * 用户登录事件
 */
@Data
public class UserLoginEvent {
    String telephone;
    String password;
}
