package com.lihe.pojo;

import lombok.Data;

import java.util.Date;

/**
 * Created by trimup on 2016/9/13.
 */
@Data
public class NoticePojo {
    private String   title            ;//标题
    private String   url              ;//地址
    private String   content          ;//内容
    private String   logo             ;//封面
    private String  date;
}
