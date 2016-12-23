package com.lihe.event.register;

import lombok.Data;

/**
 * Created by trimup on 2016/7/22.
 * 基金开户请求事件
 */
@Data
public class ReFundAccountEvent {
    private String  bank_account       ;    //   银行账号
    private String  bank_account_name  ;    //   银行户名
    private String  bank_name          ;    //   银行名称
    private String  bank_no            ;    //   银行代码
    private String  capital_mode       ;    //   资金方式
    private String  client_full_name   ;    //   客户名称全称
    private String  client_name        ;    //   客户姓名
    private String  id_kind_gb         ;    //   证件类别
    private String  id_no              ;    //   证件号码
    private String  mobile_tel         ;    //   手机号码 标准版本中将手机作为可以唯一识别一个客户的凭证，必须填写
    private String  password           ;    // 密码
    private String trade_acco          ;    //交易账号
    private String branch_bank_no      ; //联行号
    private String client_gender  ;//客户性别
    private String detail_fund_way;//明细资金方式
}
