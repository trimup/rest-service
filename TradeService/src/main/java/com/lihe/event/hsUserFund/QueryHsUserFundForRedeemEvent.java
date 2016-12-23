/**
 * @Version 1.0.0
 * Copyright (c) 2016上海相诚金融-版权所有
 */
package com.lihe.event.hsUserFund;

import lombok.Data;

/**
 * @Class QueryHsUserFundForRedeemEvent
 * @Description
 * @Author 张超超
 * @Date 2016/9/14 14:23
 */
@Data
public class QueryHsUserFundForRedeemEvent {
    private Integer userId;
//    private String tradeAcco; //基金账号
    private Integer productId; //基金ID
    private Integer bankAccounId; //用户基金账户信息ID
}
