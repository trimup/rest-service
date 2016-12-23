/**
 * @Version 1.0.0
 * Copyright (c) 2016上海相诚金融-版权所有
 */
package com.lihe.entity;

import lombok.Data;

import java.util.Date;

/**
 * @Class UserBankAccount
 * @Description
 * @Author 张超超
 * @Date 2016/9/6 17:50
 */
@Data
public class UserBankAccount {
     private Integer id; //流水号自增id
     private Integer user_tid; //用户id
     private String bank_account; //银行帐号
     private String bank_account_name; //银行账户名
     private String bank_no; //银行代码
     private String bank_name; //银行名称
     private String id_kind_gb; //证件类型
     private String id_no; //证件号码
     private String telephone; //银行预留手机号码
     private String password; //交易密码
     private String trade_acco; //交易账号
     private String ta_acco; //ta帐号
     private Date create_time; //创建时间
}
