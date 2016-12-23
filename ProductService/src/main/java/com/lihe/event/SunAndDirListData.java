package com.lihe.event;

import com.lihe.pojo.FixIncomListPojo;
import com.lihe.pojo.SunAndDirListPojo;
import lombok.Data;

import java.util.List;

/**
 * Created by trimup on 2016/8/12.
 * 阳光私募和定向增发
 */
@Data
public class SunAndDirListData {
    private int page;//第几页
    private int pageSize;//每页大小
    private long total;//总数
    private int totalPage;//总页数
    private List<SunAndDirListPojo> list;
}
