package com.lihe.replay.userInvest;

import lombok.Data;

/**
 * Created by trimup on 2016/9/23.
 */
@Data
public class ModifyBonusReply {
    private String  code	    ;//  成功：ETS-5BP0000失败：其他
	private String  allot_no  ;//	申请编号	S	24	0	N	v4.0.0.0
    private String  apply_date  ;//	申请日期	N	8	0	N	v4.0.0.0		该笔交易的申请日期；比如下单日期时间是周一22点；那么申请日期是周二；
    private String  message	    ;//  返回错误信息
}
