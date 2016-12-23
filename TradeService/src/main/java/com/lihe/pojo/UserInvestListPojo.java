package com.lihe.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by trimup on 2016/9/9.
 */
@Data
public class UserInvestListPojo {
    private String  fund_name;  //基金名称
    private String  fund_code;  //基金代码
    private BigDecimal have_money =new BigDecimal(0);  //持有金额
    private BigDecimal business_share =new BigDecimal(0);//发生份额
    private String  allot_no;//申请编号
    private String  status ;  //状态
    private Integer sort_type;
    private Date order_date;//订单日期
}
