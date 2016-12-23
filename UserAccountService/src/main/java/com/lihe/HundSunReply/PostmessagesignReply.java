package com.lihe.HundSunReply;

import lombok.Data;

/**
 * Created by trimup on 2016/7/27.
 */
@Data
public class PostmessagesignReply {
    private String  code            ;  // 返回错误码
    private String  bank_err_code   ;  //银行错误码
    private String  bank_err_msg    ;  //银行错误信息
    private String  cd_card         ;  //银联CD卡号
    private String  message         ;  //返回错误信息
    private String  ofund_user_type ;  //用户类型

}
