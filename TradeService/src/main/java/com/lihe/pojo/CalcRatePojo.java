package com.lihe.pojo;

import lombok.Data;

/**
 * Created by trimup on 2016/10/21.
 * 计算手续费对象
 */
@Data
public class CalcRatePojo {
    private String rate;//费率
    private String  confirm_date;//确认时间
    private String fare_sx ;//手续费
}
