/**
 * @Version 1.0.0
 * Copyright (c) 2016上海相诚金融-版权所有
 */
package com.lihe.replay.hsTrade;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Class TradeApplyQueryPo
 * @Description
 * @Author 张超超
 * @Date 2016/9/11 16:26
 */
@Data
public class TradeApplyQueryPo {
        private Integer accept_time; 	//下单时间 	N 	6 	0 	N 	v4.0.0.0
        private BigDecimal	after_discount_rate; 	//后收费折扣率 	F 	5 	4 	N 	v4.0.0.0
        private String	allot_no; 	//申请编号 	S 	24 	0 	N 	v4.0.0.0
        private Integer	apply_date; 	//申请日期 	N 	8 	0 	N 	v4.0.0.0 		该笔交易的申请日期；比如下单日期时间是周一22点；那么申请日期是周二；
        private Integer	apply_time; 	//申请时间 	N 	6 	0 	N 	v4.0.0.0
        private Character auto_buy; 	//分红方式 	C 	1 	0 	N 	v4.0.0.0
        private BigDecimal	balance; 	//发生金额 	N 	16 	2 	N 	v4.0.0.0
        private String	bank_account; 	//银行账号 	S 	28 	0 	N 	v4.0.0.0
        private String	bank_no; 	//银行代码 	S 	3 	0 	N 	v4.0.0.0
        private String	busin_ass_code; 	//业务辅助代码 	S 	2 	0 	N 	v4.0.0.0 		参照字典【业务辅助代码】
        private String	busin_board_type; 	//业务大类 	S 	2 	0 	N 	v4.0.0.0 		参照字典表[业务大类]
        private String	capital_mode; 	//资金方式 	S 	2 	0 	N 	v4.0.0.0 		参见【资金方式】字典
        private String	combined_error_info; 	//银联错误原因 	S 	255 	0 	N 	v4.0.0.0
        private Character	confirm_flag; 	//确认标志 	C 	1 	0 	N 	v4.0.0.0 		参见【交易确认标志】字典
        private String	cyber_bank_error_id; 	//网银错误代码 	S 	8 	0 	N 	v4.0.0.0
        private Character	deduct_status; 	//扣款状态 	C 	1 	0 	N 	v4.0.0.0 		参照【申请校验】字典
        private String	detail_fund_way; 	//明细资金方式 	S 	2 	0 	N 	v4.0.0.0
        private BigDecimal discount_rate; 	//折扣比率 	F 	9 	8 	N 	v4.0.0.0
        private Integer	expiry_date; 	//终止日期 	N 	8 	0 	N 	v4.0.0.0
        private Integer	first_exchdate; 	//首次交易日期 	N 	8 	0 	N 	v4.0.0.0
        private String	fix_date; 	//定投日 	S 	2 	0 	N 	v4.0.0.0
        private String	fund_busin_code; 	//业务代码 	S 	3 	0 	N 	v4.0.0.0 		参照【查询业务类别】字典
        private String	fund_code; 	//基金代码 	S 	6 	0 	N 	v4.0.0.0
        private Character	fund_exceed_flag; 	//巨额赎回标志 	C 	1 	0 	N 	v4.0.0.0
        private Character	fund_froze_flag; 	//冻结方式 	C 	1 	0 	N 	v4.0.0.0
        private String	fund_name; 	//基金名称 	S 	32 	0 	N 	v4.0.0.0
        private Character	fund_risk_flag; 	//风险标志 	C 	1 	0 	N 	v4.0.0.0
        private Integer	max_succ_times; 	//最大成功次数 	N 	10 	0 	N 	v4.0.0.0
        private BigDecimal	max_trade_succ_quota; 	//最大交易成功额度 	N 	16 	2 	N 	v4.0.0.0
        private String	money_type; 	//币种类别 	S 	3 	0 	N 	v4.0.0.0
        private Character	ofund_type; 	//基金类型 	C 	1 	0 	N 	v4.0.0.0
        private String	oppo_agency; 	//对方销售商 	S 	3 	0 	N 	v4.0.0.0
        private String	oppo_fund_account; 	//对方基金账号 	S 	12 	0 	N 	v4.0.0.0
        private String	oppo_netno; 	//对方销售网点编号 	S 	9 	0 	N 	v4.0.0.0
        private String	oppo_trade_account; 	//对方交易账号 	S 	17 	0 	N 	v4.0.0.0
        private String	original_appno; 	//原申请单编号 	S 	24 	0 	N 	v4.0.0.0
        private Integer	original_date; 	//下单日期 	N 	8 	0 	N 	v4.0.0.0
        private String	protocol_name; 	//协议名称 	S 	40 	0 	N 	v4.0.0.0
        private Character	protocol_period_unit; 	//协议周期单位 	C 	1 	0 	N 	v4.0.0.0
        private BigDecimal	range; 	//级差 	F 	7 	4 	N 	v4.0.0.0
        private String	receivable_account; 	//回款账户 	S 	28 	0 	N 	v4.0.0.0
        private BigDecimal	reserve_discount_rate; 	//补差费折扣率 	F 	5 	4 	N 	v4.0.0.0
        private String	scheduled_protocol_id; 	//定投协议号 	S 	20 	0 	N 	v4.0.0.0
        private String	send_flags; 	//发送标志 	S 	16 	0 	N 	v4.0.0.0
        private Character	share_type; 	//份额分类 	C 	1 	0 	N 	v4.0.0.0 		A|B
        private BigDecimal	shares; 	//发生份额 	N 	16 	2 	N 	v4.0.0.0
        private String	ta_acco; 	//TA账号/基金账号 	S 	12 	0 	N 	v4.0.0.0
        private Character	target_fund_code; 	//目标基金代码 	C 	1 	0 	N 	v4.0.0.0
        private Character	target_fund_type; 	//目标基金类型 	C 	1 	0 	N 	v4.0.0.0
        private String	trade_acco; 	//交易账号 	S 	17 	0 	N 	v4.0.0.0
        private Integer	trade_period; 	//交易周期 	S 	8 	0 	N 	v4.0.0.0
}
