package com.lihe.HundSunReply;

import lombok.Data;

/**
 * Created by trimup on 2016/7/28.
 */
@Data
public class FixTradePwdReply {
    private String  code      ;  //成功：ETS-5BP0000失败：其他
    private String  allot_no    ;// 申请编号
    private String  message     ;//

}
