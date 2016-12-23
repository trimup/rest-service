package com.lihe.event.register;

import com.google.common.base.Strings;
import lombok.Data;

/**
 * Created by trimup on 2016/7/27.
 * 添加银行卡事件
 */
@Data
public class AddBankCardEvent {
    private Integer user_tid;  //用户id
    private String  bank_account       ;    //   银行账号
    private String  bank_account_name  ;    //   银行户名
    private String  bank_name          ;    //   银行名称
    private String  bank_no            ;    //   银行代码
    private String  id_no              ;    //   证件号码
    private String  telephone         ;    //   手机号码 标准版本中将手机作为可以唯一识别一个客户的凭证，必须填写
    private String  password           ;    // 密码
    private String  verifyCode         ;   //验证码
    private String  allot_no; //请求验证码编号
    private String  other_serial;//对方流水号


    //身份证转换成大写的
    public String getId_no(){
        if(Strings.isNullOrEmpty(this.id_no))
            return "";
        return  this.id_no.toUpperCase();
    }



    /**
     * 验证基金开户参数
     * @return
     */
    public boolean checkReFundAccount(){
        return user_tid==null||Strings.isNullOrEmpty(bank_account)
                ||Strings.isNullOrEmpty(bank_account_name)||Strings.isNullOrEmpty(bank_name)
                ||Strings.isNullOrEmpty(bank_no)
                ||Strings.isNullOrEmpty(id_no)|| id_no.length()<18||
                Strings.isNullOrEmpty(telephone)
                ||Strings.isNullOrEmpty(verifyCode)
                ||Strings.isNullOrEmpty(allot_no)
                ||Strings.isNullOrEmpty(other_serial)
                ||Strings.isNullOrEmpty(password);
    }

    /**
     * 验证 请求 基金开会验证码
     */
    public boolean checkReVerifyCode(){
        return  Strings.isNullOrEmpty(bank_account)
                ||Strings.isNullOrEmpty(bank_account_name)||Strings.isNullOrEmpty(bank_name)
                ||Strings.isNullOrEmpty(bank_no)
                ||Strings.isNullOrEmpty(id_no)||id_no.length()<18||Strings.isNullOrEmpty(telephone);
    }

    /**
     * 验证 增开交易账户
     */
    public boolean checkaddTradeAccount(){
        return user_tid==null||Strings.isNullOrEmpty(bank_account)
                ||Strings.isNullOrEmpty(bank_account_name)||Strings.isNullOrEmpty(bank_name)
                ||Strings.isNullOrEmpty(bank_no)
                || Strings.isNullOrEmpty(telephone)
                ||Strings.isNullOrEmpty(verifyCode)
                ||Strings.isNullOrEmpty(allot_no)
                ||Strings.isNullOrEmpty(other_serial);
    }


}
