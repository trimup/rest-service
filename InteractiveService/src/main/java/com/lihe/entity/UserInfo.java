package com.lihe.entity;

import lombok.Data;

/**
 * Created by trimup on 2016/7/21.
 */
@Data
public class UserInfo {
     private Integer  id                   ; //          ID',
     private String  user_name            ; //    用户昵称',
     private String  real_name            ; //    用户真名',
     private String  identity_card        ; //    用户身份证号',
     private String  telephone            ; //    用户手机号码',
     private Integer sex                  ; //    性别，1代表男，2代表女，3代表未知',
     private Integer age                  ; //    年龄',
     private String  brithday             ; //    生日',
     private Long    referrer_id          ; //   '推荐人id',
     private Long    financial_advisor_id ; //   '专属理财师',
     private String  login_password       ; //   '登录密码',
     private String  trade_password       ; //   '基金交易密码',
     private String  token                ; //    token
     private String  status               ; //   '用户状态，1代表用户被冻结，2代表用户可用',
     private Integer  errortips            ; //   '误错次数',
     private String  register_type        ; //   '注册渠道，1代表pc，2代表wechat，3代表ios，4代表android',
     private String  register_channel     ; //   '用户注册渠道',     默认 liheapp
     private String  create_time          ; //   '用户信息创建时间',
}
