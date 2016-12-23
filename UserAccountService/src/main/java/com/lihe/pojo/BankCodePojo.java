package com.lihe.pojo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by trimup on 2016/8/22.
 */
@Data
public class BankCodePojo {
    String  bank_name;//银行名称
    String hs_code ;//银行代码
    BigDecimal bank_day_limit;//每日限额
    BigDecimal bank_per_limit ;// 每次限额
}
