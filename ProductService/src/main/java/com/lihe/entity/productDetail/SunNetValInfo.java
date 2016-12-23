package com.lihe.entity.productDetail;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by trimup on 2016/9/13.
 * 阳光私募基金净值
 */
@Data
public class SunNetValInfo {
    private Integer id;
    private Integer product_id;
    private Date riqi;
    private BigDecimal ljsy;
    private BigDecimal dwjz;
    private Date create_time;

}
