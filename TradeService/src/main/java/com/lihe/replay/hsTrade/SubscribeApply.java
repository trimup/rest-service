/**
 * @Version 1.0.0
 * Copyright (c) 2016上海相诚金融-版权所有
 */
package com.lihe.replay.hsTrade;

import lombok.Data;

/**
 * @Class SubscribeApply
 * @Description 认购返回数据
 * @Author 张超超
 * @Date 2016/9/5 17:25
 */
@Data
public class SubscribeApply {
       private String code;
       private String message;
       private String details;
       private String allot_no;
       private String balance;
       private String clear_date;
       private String apply_time;
       private String apply_date;
}
