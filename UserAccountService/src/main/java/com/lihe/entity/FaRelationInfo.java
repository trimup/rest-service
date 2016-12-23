package com.lihe.entity;

import lombok.Data;

/**
 * Created by trimup on 2016/8/2.
 */
@Data
public class FaRelationInfo {
    private Integer id                   ;     //'主键ID',
    private Integer financial_advisor_id ;    // 理财师id对应,利和后台user_info',
    private Integer lh_app_user_id       ;   //利和的app id
    private Integer width                ;    //维度
    private Integer create_time          ;   //创建时间
}
