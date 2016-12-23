package com.lihe.pojo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by trimup on 2016/8/12.
 */
@Data
public class SunAndDirListPojo {
    private  Integer   id                   ;  //项目id
    private String product_code; //
    private  String   product_name         ;  //项目名称
    private BigDecimal product_min_invest   ; //项目起投金额
}
