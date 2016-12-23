package com.lihe.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created by trimup on 2016/7/21
 * 短信日志
 */
@Data
public class SmsLogInfo {
    private Integer id                   ;
    private String mobile                ;
    private String content               ;
    private String return_code           ;
    private Date create_time             ;
}
