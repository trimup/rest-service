package com.lihe.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by trimup on 2016/9/26.
 */
@Data
public class UserIncomeInfo {
    private Integer id;
    private Integer user_tid;
    private BigDecimal income;
    private String  income_date;
    private Date create_time;
}
