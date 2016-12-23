package com.lihe.event.userInvest;

import com.lihe.pojo.UserIncomePojo;
import lombok.Data;

import java.util.List;

/**
 * Created by trimup on 2016/9/27.
 */
@Data
public class UserIncomeData {
    private int page;//第几页
    private int pageSize;//每页大小
    private long total;//总数
    private int totalPage;//总页数
    private List<UserIncomePojo> list;
}
