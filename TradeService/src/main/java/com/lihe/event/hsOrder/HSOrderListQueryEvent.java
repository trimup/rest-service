/**
 * @Version 1.0.0
 * Copyright (c) 2016上海相诚金融-版权所有
 */
package com.lihe.event.hsOrder;

import com.lihe.pojo.HSOrderPojo;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * @Class HSOrderListQueryEvent
 * @Description
 * @Author 张超超
 * @Date 2016/9/28 13:07
 */
@Data
public class HSOrderListQueryEvent {
    private Integer pageSize;
    private Integer page;
    List<HSOrderPojo> list = Collections.emptyList();
}
