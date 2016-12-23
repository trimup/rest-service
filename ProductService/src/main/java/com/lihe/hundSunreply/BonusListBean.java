package com.lihe.hundSunreply;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by trimup on 2016/9/23.
 */
@Data
public class BonusListBean {
    private String  auto_buy  ;   //	分红方式
    private String   bank_account;         //	银行账号
    private String   bank_name;  //	银行名称
    private String   bank_no;   //	银行代码
    private BigDecimal business_frozen_shares;  //	交易冻结份额
    private BigDecimal   current_share;   //	当前份额
    private BigDecimal   enable_shares;   //	可用份额
    private String   forbid_modi_autobuy_flag;//	禁止修改分红方式标志
    private BigDecimal   frozen_shares;   //	冻结份额
    private String   fund_code;   //	基金代码
    private String   fund_name;  //	基金名称
    private String   fund_status ;  //	基金状态
    private String   ofund_risklevel;  //	基金风险等级
    private String   ofund_type;   //	基金类型
    private String   share_type;  //	份额分类
    private String   ta_acco;   //	TA账号/基金账号
    private String   ta_no;   //	TA编号
    private String   trade_acco	;   //交易账号
    private BigDecimal   unpaid_income;  //	未付收益金额
}
