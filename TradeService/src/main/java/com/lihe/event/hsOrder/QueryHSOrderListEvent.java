/**
 * @Version 1.0.0
 * Copyright (c) 2016上海相诚金融-版权所有
 */
package com.lihe.event.hsOrder;

import com.google.common.base.Strings;
import lombok.Data;

/**
 * @Class QueryHSOrderListEvent
 * @Description
 * @Author 张超超
 * @Date 2016/9/28 13:05
 */
@Data
public class QueryHSOrderListEvent {
    private Integer userId;
    private String token;
    private String  fund_code;
    private Integer pageSize =5;
    private Integer page =1;

    public boolean orCheck(){
        return  userId==null|| Strings.isNullOrEmpty(token)||Strings.isNullOrEmpty(fund_code);
    }
}
