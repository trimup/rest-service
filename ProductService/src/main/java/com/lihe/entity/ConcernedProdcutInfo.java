package com.lihe.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created by trimup on 2016/9/2.
 */
@Data
public class ConcernedProdcutInfo {
    private Integer id;
    private String pro_code; //基金代码
    private Integer type;   //类型 1公募 2私募
    private Integer user_tid; //用户id
    private Integer status;//状态 1关注 0为取消关注
    private Date create_time;//创建时间
}
