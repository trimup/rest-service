package com.lihe.replay.hsTrade;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by trimup on 2016/10/21.
 */
@Data
public class FeecalQueryApply {
    private String  code             ;//返回code
    private String  message          ;//返回消息
    private String  details          ;//返回详情
    private String  discount_rate    ;//费率折扣率
    private String  bank_no          ;//银行编号
    private String  fare_sx          ;//手续费
    private BigDecimal rate             ;//费率
    private String  bank_name        ;//银行名称
    private String  bank_account     ;//银行账号
    private String  pay_mode         ;//支付方式
    private String  capital_mode     ;//支付渠道
    private String  ori_fare         ;//折扣前费用
    private String  ori_fare_ratio   ;//折前费率
}
