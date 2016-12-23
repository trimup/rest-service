/**
 * @Version 1.0.0
 * Copyright (c) 2016上海相诚金融-版权所有
 */
package com.lihe.event.hsTrade;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Class PurchaseProductEvent
 * @Description
 * @Author 张超超
 * @Date 2016/9/6 17:19
 */
@Data
public class PurchaseProductEvent {
    private Integer userId; //用户ID
    private String token; //token
    private Integer bankAccounId; //用户基金账户信息ID
    private String password; //支付密码
//    private String trade_acco;
    private BigDecimal balance; //购买金额
    private Integer hsProductId; //基金ID
//    private String fundCode; //基金编码
}
