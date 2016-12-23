package com.lihe.entity;

import lombok.Data;

/**
 * Created by trimup on 2016/8/18.
 */
@Data
public class AdsCfgInfo {
    private String  id;                        //id
    private String  channel;                  //渠道 1 APP 2PC
    private String  type;                   // 类型 1平台活动2宣传活动
    private String  is_view;                //  首页显示：1是 0否
    private String  sort;                 // 排序
    private String  start_time;         // 开始时间
    private String  end_time;          // 结束时间
    private String  is_online;          //是否上架 1,上，2否
    private String  name;             //活动名称
    private String  url;             //活动地址
    private String  img;             //图片
    private String  img_small;      //图片缩略
    private String  create_time;   //创建时间
}
