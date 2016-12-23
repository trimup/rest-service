package com.lihe.entity;

import lombok.Data;

/**
 * Created by trimup on 2016/7/27.
 * 银行账户信息
 */
@Data
public class BankAccountInfo {
    private Integer id                 ; //      id
    private Integer user_tid           ; //      用户id
    private String  bank_account       ; //      银行账户
    private String  bank_account_name  ; //      银行账户名
    private String  bank_no            ; //      银行代码
    private String  bank_name          ; //      银行名称
    private String  id_kind_gb         ; //      证件类型
    private String  id_no              ; //      证件号码
    private String  telephone          ; //      银行开户手机号码     s
    private String  password           ; //      交易密码
    private String  trade_acco         ;  //     交易账号
    private String  ta_acco            ;  //TA帐号
    private String  branch_bank_no;  //联行号
    private String  capital_mode; //资金方式
    private String  detail_fund_way;// 详细资金方式
    private String  capital_mode_name;//资金方式名称
}
