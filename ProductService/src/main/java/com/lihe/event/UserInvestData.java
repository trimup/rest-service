package com.lihe.event;

import com.lihe.pojo.FixIncomListPojo;

import java.util.List;

/**
 * Created by trimup on 2016/9/6.
 */
public class UserInvestData {
    private int page;//第几页
    private int pageSize;//每页大小
    private long total;//总数
    private int totalPage;//总页数
    private List<FixIncomListPojo> list;
}
