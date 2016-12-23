/**
 * @Version 1.0.0
 * Copyright (c) 2016上海相诚金融-版权所有
 */
package com.lihe.event.hsTrade;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Class RedeemProductEvent
 * @Description
 * @Author 张超超
 * @Date 2016/9/13 9:53
 */
@Data
public class RedeemProductEvent {
    private Integer userId; //用户ID
    private String token; //token
    private Integer bankAccounId; //用户基金账户信息ID
    private String password; //支付密码
    private BigDecimal shares; //赎回份额
    private Integer hsProductId; //基金ID
}
