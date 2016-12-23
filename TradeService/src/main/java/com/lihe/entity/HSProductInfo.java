package com.lihe.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by trimup on 2016/8/8.
 * 共计基金
 *
 */
@Data
public class HSProductInfo implements Serializable {

//    private Integer id                        ; //项目id
//    private String  accept_hq_date	          ; //  接收行情日期	N	8	0	N	v4.0.0.0
//    private String  forbid_modi_autobuy_flag  ;	// 禁止修改分红方式标志	C	1	0	N	v4.0.0.0
//    private String  fund_code	              ; //  基金代码	S	6	0	N	v4.0.0.0
//    private String  fund_contract_flag	      ; //  是否需要签署电子合同	C	1	0	N	v4.0.0.0		1-需要签署,空或者0-不需要签署
//    private BigDecimal fund_curr_income	      ; //  基金收益	F	16	2	N	v4.0.0.0
//    private BigDecimal  fund_curr_ratio	          ; //  基金收益率	F	10	5	N	v4.0.0.0
//    private String  fund_full_name	          ; //  基金全称	S	100	0	N	v4.0.0.0
//    private String  fund_name	              ; //  基金名称	S	40	0	N	v4.0.0.0
//    private BigDecimal  fund_share	              ; //  基金总份额	F	16	2	N	v4.0.0.0
//    private String  fund_status	              ; //  基金状态	C	1	0	N	v4.0.0.0
//    private String  fund_sub_type	          ; //  基金子类型	C	1	0	N	v4.0.0.0
//    private String  hq_date	                  ; //  行情日期	N	8	0	N	v4.0.0.0
//    private BigDecimal  nav	                      ; //  T-1日基金单位净值	F	8	5	N	v4.0.0.0
//    private BigDecimal  nav_total	              ; //  累计净值	F	7	4	N	v4.0.0.0
//    private String  ofund_risklevel	          ; //  基金风险等级	C	1	0	N	v4.0.0.0
//    private String  ofund_type	              ; //  基金类型	C	1	0	N	v4.0.0.0
//    private BigDecimal  per_myriad_income	      ; //  万份单位收益	F	10	5	N	v4.0.0.0
//    private String  pre_income_ratio	      ; //  预期年化收益率	F	10	5	N	v4.0.0.0
//    private String  prod_term	              ; //  产品期限	S	64	0	N	v4.0.0.0
//    private String  share_type	              ; //  份额分类	C	1	0	N	v4.0.0.0
//    private String  ta_no	                  ; //  TA编号	S	4	0	N	v4.0.0.0     ;   //


    private Integer  id;
    private Integer    declare_endday	    ;   //       预期申购截至日	N	8	0	N	v4.0.0.0
    private String    declare_state	        ;   //   申购状态	C	1	0	N	v4.0.0.0		1：可申购
    private Integer    expire_date	        ;   //       到期日	N	8	0	N	v4.0.0.0
    private String    frozen_limit	        ;   //   冻结额度	C	1	0	N	v4.0.0.0
    private String    fund_code 	        ;   //       基金代码	S	6	0	N	v4.0.0.0
    private BigDecimal fund_curr_ratio	    ;   //       七日年化收益率	F	9	4	N	v4.0.0.0
    private String    fund_full_name	    ;   //       基金全称	S	100	0	N	v4.0.0.0
    private String    fund_name	            ;   //   基金名称	S	20	0	N	v4.0.0.0
    private String    fund_status	        ;   //       基金状态	C	1	0	N	v4.0.0.0		字典[基金状态]
    private String    fundStatus            ;   //    基金状态
    private String    fund_sub_type	        ;   //   基金子类型	C	1	0	N	v4.0.0.0		字典[基金子类型]
    private String    hopedeclare_state	    ;   //   预约申购状态	C	1	0	N	v4.0.0.0		1：可预约申购
    private Integer    issue_date	        ;   //       基金发行日	N	8	0	N	v4.0.0.0
    private String    manager_code	        ;   //   基金管理人代码	S	8	0	N	v4.0.0.0
    private String    manager_name	        ;   //   基金管理人名称	S	36	0	N	v4.0.0.0
    private BigDecimal    min_share	            ;   //   基金最小持有份额	F	16	2	N	v4.0.0.0
    private Integer    nav_date	            ;   //   净值日期	N	8	0	N	v4.0.0.0
    private BigDecimal    nav_total	            ;   //   累计净值	F	11	4	N	v4.0.0.0
    private BigDecimal    net_value	            ;   //   基金净值	F	11	4	N	v4.0.0.0
    private Integer    ofund_risklevel	    ;   //       基金风险等级	N	8	0	N	v4.0.0.0
    private String   ofundRisk ;         //基金风险等级
    private String    ofund_type	        ;   //       基金类别	C	1	0	N	v4.0.0.0		字典[基金类型]
    private String    ofundType      ;//       基金类别
    private Integer    open_date	            ;   //   产品开放日	N	8	0	N	v4.0.0.0
    private BigDecimal    per_myriad_income	    ;   //   万份基金单位收益	F	16	5	N	v4.0.0.0
    private BigDecimal    pre_income_ratio	    ;   //   预期年化收益率	F	9	4	N	v4.0.0.0
    private BigDecimal    pre_yield	            ;   //   收益率	F	9	4	N	v4.0.0.0
    private Integer    product_due_time	    ;   //   产品期限	N	5	0	N	v4.0.0.0
    private Integer    redeem_endday	        ;   //   预期赎回截至日	N	8	0	N	v4.0.0.0
    private String    redeem_state	        ;   //   赎回状态	C	1	0	N	v4.0.0.0		1：可赎回
    private String    remain_limit	        ;   //   剩余额度	C	1	0	N	v4.0.0.0
    private Character    share_type	        ;   //       份额类别	C	1	0	N	v4.0.0.0		字典[份额类别]
    private String    subscribe_state	    ;   //       认购状态	C	1	0	N	v4.0.0.0		1：可认购
    private String    support_limit_control	;   //   是否支持根据交易控制基金规模	C	1	0	N	v4.0.0.0		1：支持根据交易控制基金规模
    private String    ta_code	            ;   //       TA代码	S	4	0	N	v4.0.0.0
    private String    trans_state	        ;   //       转换出状态	C	1	0	N	v4.0.0.0		1：可转换出
    private String    valuagr_state	        ;   //   定投状态	C	1	0	N	v4.0.0.0		1：可定投
    private BigDecimal start_money ;//起投金额
    private String    jbxx          ;//  基本信息 序列化处理 托管银行：tgyy 成立时间： clsj
    private String    zcpz         ; //资产配置 序列化处理 股票:gp 债券:zj 银行:yh 其他:qt 总规模:zgm
    private String    zycc         ; //主要持仓 序列化处理 名称:ccmc 代码:ccdm 市值:ccsz 配置占比:ccpezb
    private String    tzfw        ;//投资范围
    private String    tzmb   ;// NULL投资目标
    private String  jjjl        ;// NULL基金经理 序列化处理 图片:jllogo 姓名:jlxm 内容:jlnr
    private String  fl      ;//序列化处理 申请确认时间(预估):sqsj 赎回确认时间(预估):shsj 基金管理费:jjglf 基金托管费:jjtgf 销售服务费:xsfwf
    private BigDecimal  yj ;//  NULL佣金
    private Integer auto_buy; //分红方式（0：红利再投资； 1：现金红利）






}
