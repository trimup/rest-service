package com.lihe.entity;

import lombok.Data;

/**
 * Created by trimup on 2016/6/16.
 */
@Data
public class UserShareInfo {
    private Integer  id                 ;
    private Integer  share_user_tid     ;    //  分享链接的用户
    private String  extend_type    ;  // 推广的类型（0:微信，1：微信朋友圈2：短信3：新浪4：二维码）
    private Integer    random             ;   //随机数
    private Long  end_time;//活动结束时间
    private String  create_time        ;
}
