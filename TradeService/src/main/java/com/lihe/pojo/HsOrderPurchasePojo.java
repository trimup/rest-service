/**
 * @Version 1.0.0
 * Copyright (c) 2016上海相诚金融-版权所有
 */
package com.lihe.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Class HSOrderPurchasePojo
 * @Description 公募交易订单表
 * @Author 张超超
 * @Date 2016/9/5 13:41
 */
@Data
public class HsOrderPurchasePojo {
    private Integer id;
    private String allot_no; //申请编号
    private String fund_code; //基金代码
    private String fund_name; //基金名称
    private Integer trade_type; //交易类型(0:申购;1:认购;2:赎回)
    private String trade_type_str; //交易类型
    private String trade_acco; //交易账号
    private Integer user_tid; //用户ID

    private BigDecimal balance; //发生金额(申购、认购)
//    private BigDecimal apply_shares; //申请份额(申购、认购)

    private BigDecimal shares; //发生份额(赎回)

//    private char share_type; //份额类型(A前收费 B后收费)
    private Integer trade_status_tid; //交易状态
    private String trade_status; //交易状态

    private Date create_time; //创建时间

//    private Date apply_time; //申请时间
//    private Date clear_date; //清算时间
}
