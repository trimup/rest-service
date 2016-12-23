package com.lihe.HundSunReply;

import lombok.Data;

/**
 * Created by trimup on 2016/8/4.
 * 增开交易帐号返回
 */
@Data
public class OpenTradeReply {
    private String  code;    //返回错误码
    private String  allot_no;  //申请编号
    private String invest_risk_tolerance ; //投资人风险承受能力
    private String message;  //返回错误信息
    private String trade_acco; //交易帐号


}
