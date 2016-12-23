package com.lihe.pojo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by trimup on 2016/8/17.
 * 查询用户信息服务
 */
@Data
public class UserBankPojo {


    Integer id;
    String  bank_name;//银行名称
    String bank_code ;//银行代码
    String  bank_account;//银行账户
    BigDecimal bank_day_limit;//每日限额
    BigDecimal bank_per_limit ;// 每次限额
}
