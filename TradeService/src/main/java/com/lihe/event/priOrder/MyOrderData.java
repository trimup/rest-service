package com.lihe.event.priOrder;

import com.lihe.pojo.MyOrderListPojo;
import lombok.Data;

import java.util.List;

/**
 * Created by trimup on 2016/9/7.
 * 我的预约（私募）
 */
@Data
public class MyOrderData {
    private int page;//第几页
    private int pageSize;//每页大小
    private long total;//总数
    private int totalPage;//总页数
    private List<MyOrderListPojo> list;
}
