package com.lihe.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by trimup on 2016/8/22.
 */
@Data
public class HSBankCodeInfo {
    private Integer   id              ;  //    int(10) NOT NULL
    private String   bank_code       ;  //    varchar(20) NOT NULL银行代码
    private String   hs_code         ;  //    varchar(15) NOT NULL恒生代码
    private String   bank_name       ;  //    varchar(30) NOT NULL银行名称
    private String   money_type      ;  //    varchar(30) NOT NULL资金方式
    private String   money_type_code      ;  //    varchar(30) NOT NULL资金方式
    private BigDecimal day_limit       ;  //    decimal(16,4) NOT NULL日限额
    private BigDecimal   mon_limit       ;  //    decimal(16,4) NOT NULL月限额
    private BigDecimal   one_limit       ;  //    decimal(16,4) NOT NULL单笔限额
    private Integer   status          ;  //    tinyint(1) NOT NULL状态 0禁用 1启用
    private String branch_bank_no;  //   联行号
    private Date create_time     ;  //    timestamp NOT NULL创建时间
}
