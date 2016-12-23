package com.lihe.entity.productDetail;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by trimup on 2016/8/25.
 * //基金费率
 */
@Data
public class FundRateinfo {
    private Integer  id              ;    //    主键ID
    private String  fund_code       ;    //    基金代码
    private String  amount_range    ;    //    金额区间
    private String orgiginal_rate  ;    //    原始费率
    private String  preferred_rate  ;    //    优惠费率
    private Integer  rate_type       ;    //    费率类型 1申购(前端)2申购(后端)3认购(前端)4认购(后端)5赎回
    private Date update_time   ;// 更新时间

}
