package com.lihe.pojo;

import lombok.Data;

/**
 * Created by Administrator on 2015/11/11.
 */
@Data
public class ContentQueryBean
{
    private Long id ;// user_msg Id
    private  String  title            ; //  标题
    private  String  content          ; //  内容
    private  String  pate_title       ; //  图片地址
    private  String  create_time      ; //  创建时间
    private  Integer  is_read   ;// 消息是否读过 1读过 0未读
    private  String  subtitle         ; //  L
}
