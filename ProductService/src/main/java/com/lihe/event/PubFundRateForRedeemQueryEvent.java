/**
 * @Version 1.0.0
 * Copyright (c) 2016上海相诚金融-版权所有
 */
package com.lihe.event;

import com.lihe.pojo.productDetail.RedeemRatePojo;
import lombok.Data;

import java.util.List;

/**
 * @Class PubFundRateForRedeemQueryEvent
 * @Description
 * @Author 张超超
 * @Date 2016/9/22 15:06
 */
@Data
public class PubFundRateForRedeemQueryEvent {
    private String rateRange; //利率范围
    private List<RedeemRatePojo> list  ;//赎回费率
}
