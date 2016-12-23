package com.lihe.pojo.userInvest;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by trimup on 2016/9/6.
 */
@Data
public class UserInvestPojo {
    String  fund_name; //基金名称
    String  fund_code;  //基金代码
    BigDecimal blance ;  //发生金额
}
