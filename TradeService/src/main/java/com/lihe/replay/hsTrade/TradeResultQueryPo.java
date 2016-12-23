/**
 * @Version 1.0.0
 * Copyright (c) 2016上海相诚金融-版权所有
 */
package com.lihe.replay.hsTrade; //

import lombok.Data;

import java.math.BigDecimal; //

/**
 * @Class TradeResultQueryPo
 * @Description
 * @Author 张超超
 * @Date 2016/9/8 15:07
 */
@Data
public class TradeResultQueryPo {
        private Integer affirm_date; // 	//确认日期 	N 	8 	0 	N 	v4.0.0.0
        private String allot_no; // 	//申请编号 	S 	24 	0 	N 	v4.0.0.0
        private Integer apply_date; // 	申请日期 	N 	8 	0 	N 	v4.0.0.0
        private BigDecimal apply_share; // 	申请份数 	F 	16 	2 	N 	v4.0.0.0
        private Integer apply_time; // 	申请时间 	N 	6 	0 	N 	v4.0.0.0
        private Character auto_buy; // 	分红方式 	C 	1 	0 	N 	v4.0.0.0
        private BigDecimal balance; // 	申请金额 	F 	16 	2 	N 	v4.0.0.0
        private String bank_account; // 	银行帐号 	S 	19 	0 	N 	v4.0.0.0
        private String bank_protocol_id; // 	银行协议号 	S 	60 	0 	N 	v4.0.0.0
        private Integer clear_date; // 	清算日期 	N 	8 	0 	N 	v4.0.0.0
        private String combined_error_info; // 	银联错误原因 	S 	255 	0 	N 	v4.0.0.0
        private Character confirm_state; // 	确认状态 	C 	1 	0 	N 	v4.0.0.0
        private BigDecimal current_share; // 	当前份额 	N 	16 	2 	N 	v4.0.0.0
        private String cyber_bank_error_id; // 	网银错误代码 	S 	8 	0 	N 	v4.0.0.0
        private Character deduct_status; // 	扣款状态 	C 	1 	0 	N 	v4.0.0.0 		直销[申请校验]字典0未校验1无效2有效3已发送扣款指令
        private BigDecimal enable_shares; // 	可用份额 	F 	16 	2 	N 	v4.0.0.0
        private BigDecimal fare_sx; // 	手续费 	N 	16 	0 	N 	v4.0.0.0
        private BigDecimal frozen_shares; // 	冻结份额 	F 	16 	2 	N 	v4.0.0.0
        private String fund_busin_code; // 	业务代码 	S 	3 	0 	N 	v4.0.0.0
        private String fund_code; // 	基金代码 	S 	6 	0 	N 	v4.0.0.0
        private String fund_name; // 	基金名称 	S 	50 	0 	N 	v4.0.0.0
        private String order_date_time; // 	下单日期时间 	S 	14 	0 	N 	v4.0.0.0
        private String original_appno; // 	原申请单编号 	S 	24 	0 	N 	v4.0.0.0
        private String sale_code; // 	销售人代码 	S 	9 	0 	N 	v4.0.0.0
        private String scheduled_protocol_id; // 	定投协议号 	S 	20 	0 	N 	v4.0.0.0
        private Character share_type; // 	份额类别 	C 	1 	0 	N 	v4.0.0.0
        private String ta_acco; // 	TA账号/基金账号 	S 	12 	0 	N 	v4.0.0.0
        private String String; // target_fund_code 	目标基金代码 	S 	6 	0 	N 	v4.0.0.0
        private Character target_share_type; // 	目标份额类型 	C 	1 	0 	N 	v4.0.0.0
        private String trade_acco; // 	交易账号 	S 	17 	0 	N 	v4.0.0.0
        private BigDecimal trade_confirm_balance; // 	交易确认金额 	N 	16 	0 	N 	v4.0.0.0
        private BigDecimal trade_confirm_shares; // 	交易确认份额 	N 	16 	0 	N 	v4.0.0.0
        private Character trade_status; // 	交易处理状态 	C 	1 	0 	N 	v4.0.0.0
}
