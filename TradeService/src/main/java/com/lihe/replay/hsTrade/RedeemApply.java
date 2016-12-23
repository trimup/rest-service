/**
 * @Version 1.0.0
 * Copyright (c) 2016上海相诚金融-版权所有
 */
package com.lihe.replay.hsTrade;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Class RedeemApply
 * @Description
 * @Author 张超超
 * @Date 2016/9/13 10:52
 */
@Data
public class RedeemApply {
private String code; //返回错误码 	S 	11 	0 	Y 	v4.0.0.0 		成功：ETS-5BP0000失败：其他
private String allot_no; //申请编号 	S 	24 	0 	N 	v4.0.0.0
private String apply_date; //申请日期 	N 	8 	0 	N 	v4.0.0.0 		该笔交易的申请日期；比如下单日期时间是周一22点；那么申请日期是周二；
private String apply_time; //申请时间 	N 	6 	0 	N 	v4.0.0.0
private String clear_date; //清算日期 	N 	8 	0 	N 	v4.0.0.0
private String curr_date; //当前日期 	N 	8 	0 	N 	v4.0.0.0
private String curr_time; //当前时间 	N 	8 	0 	N 	v4.0.0.0
private BigDecimal enable_shares; //可用份额 	F 	16 	2 	N 	v4.0.0.0
private BigDecimal frozen_shares; //冻结份额 	F 	16 	2 	N 	v4.0.0.0
private BigDecimal fund_share; //基金总份额 	F 	16 	0 	N 	v4.0.0.0
private String message; //返回错误信息 	S 	60 	0 	N 	v4.0.0.0
private BigDecimal nodefault_total_share; //不违约总份额 	F 	16 	2 	N 	v4.0.0.0
private BigDecimal nopay_income; //未付收益 	F 	16 	2 	N 	v4.0.0.0
private Character penalty_flag; //违约标志 	C 	1 	0 	N 	v4.0.0.0
private BigDecimal today_frozen_share; //当日冻结份额 	F 	16 	2 	N 	v4.0.0.0
private BigDecimal today_income; //每日收益 	F 	16 	2 	N 	v4.0.0.0
private BigDecimal today_total_share; //当日总份额 	F 	16 	2 	N 	v4.0.0.0
}
