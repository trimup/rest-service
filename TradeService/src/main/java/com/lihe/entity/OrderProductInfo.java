package com.lihe.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created by trimup on 2016/9/7.
 */
@Data
public class OrderProductInfo {
    private Integer id               ;  //
    private String user_tid         ;  //   用户ID
    private String product_code     ;  //   项目code
    private Date create_time      ;  //
}
