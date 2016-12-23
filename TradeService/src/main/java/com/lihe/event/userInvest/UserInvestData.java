package com.lihe.event.userInvest;

import com.lihe.pojo.MyInvestListPojo;
import com.lihe.pojo.UserInvestListPojo;
import lombok.Data;

import java.util.List;

/**
 * Created by trimup on 2016/9/14.
 */
@Data
public class UserInvestData {
    private int page;//第几页
    private int pageSize;//每页大小
    private long total;//总数
    private int totalPage;//总页数
    private List<MyInvestListPojo> list;
}
