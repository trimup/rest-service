package com.lihe.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created by trimup on 2016/9/13.
 */
@Data
public class NoticeInfo {
    private Integer   id   ;
    private Integer   type             ;//类型1项目公告2系统消息3活动消息
    private String   title            ;//标题
    private String   url              ;//地址
    private String   status           ;//是否生效 1是 2否
    private Date start_time       ;//开始时间
    private Date   end_time         ;//结束时间
    private String   content          ;//内容
    private String   logo             ;//封面
    private Date   create_time      ;//创建时间
    private Date   update_time      ;//修改时间
}
