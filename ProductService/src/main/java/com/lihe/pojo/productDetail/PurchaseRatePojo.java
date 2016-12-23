package com.lihe.pojo.productDetail;

import lombok.Data;

/**
 * Created by trimup on 2016/8/30.
 * 认购费率
 */
@Data
public class PurchaseRatePojo {
    String amount_range;
    String  orgiginal_rate ;//原始费率
    String  preferred_rate ;//优惠费率

}
