/**
 * @Version 1.0.0
 * Copyright (c) 2016上海相诚金融-版权所有
 */
package com.lihe.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Class HSOrderPojo
 * @Description 公募基金订单Po
 * @Author 张超超
 * @Date 2016/9/28 9:45
 */
@Data
public class HSOrderPojo {
    private BigDecimal amount; //支付金额
    private BigDecimal confirm_amount; //确认金额
    private BigDecimal shares; //份额
    private Date timeStr; //时间
    private String status; //状态
    private Integer type; //0:购买 1:赎回
}
