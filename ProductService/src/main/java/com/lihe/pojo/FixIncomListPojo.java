package com.lihe.pojo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by trimup on 2016/8/12.
 */
@Data
public class FixIncomListPojo {

    private  Integer   id                   ;  //项目id
    private String  product_code;
    private  String   product_name         ;  //项目名称
    private  BigDecimal   product_min_invest   ; //项目起投金额
    private  String   raise_start_time     ; //项目开始募资时间
    private String maxExpectProfit   ; //最大的预计收益
    private String minExpectProfit   ;//最小的预计收益
    private String   product_deadline         ;// varchar(20) NULL项目期限，按月为单位

}
