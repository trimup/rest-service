package com.lihe.event.register;

import lombok.Data;

/**
 * Created by trimup on 2016/7/27.
 * 请求基金开户验证码 事件
 */
@Data
public class FundCodeReturn {
    String allot_no ; //请求编号
    String other_serial;//对方流水号
}
