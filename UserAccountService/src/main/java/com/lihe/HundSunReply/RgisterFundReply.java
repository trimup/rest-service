package com.lihe.HundSunReply;

import lombok.Data;

/**
 * Created by trimup on 2016/7/25.
 */
@Data
public class RgisterFundReply {
    private String  code         ; // 返回错误码 成功：ETS-5BP0000失败：其他
    private String  allot_no     ; //  申请编号
    private String  apply_date   ; //  申请日期  该笔交易的申请日期；比如下单日期时间是周一22点；那么申请日期是周二；
    private String  client_id    ; //  客户编号
    private String  message      ; //  返回错误信息
    private String  ta_acco      ; //  TA账号/基金账号
    private String  trade_acco   ; //  交易账号

}
