package com.lihe.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created by trimup on 2016/7/21.
 */
@Data
public class VerifySmsInfo {

    private Integer id   ;
    private String  sms_verify_code ;
    private Date create_time     ;
    private String  telephone       ;
    private Integer  code_type       ;
}
