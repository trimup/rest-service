package com.lihe.HundSunReply;

import lombok.Data;

/**
 * Created by trimup on 2016/7/27.
 */
@Data
public class PremessAgesignReply {
    private String  code         ; // 返回错误码 成功：ETS-5BP0000失败：其他
    private String  bank_err_code     ; //  银行错误码
    private String  bank_err_msg   ; //  银行错误信息
    private String  message      ; //  返回错误信息
    private String  other_serial   ; //  对方流水号
}
