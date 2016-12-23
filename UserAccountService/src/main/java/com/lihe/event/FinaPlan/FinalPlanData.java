package com.lihe.event.FinaPlan;

import com.lihe.pojo.FPListPojo;
import lombok.Data;

import java.util.List;

/**
 * Created by trimup on 2016/9/27.
 */
@Data
public class FinalPlanData {
    private int page;//第几页
    private int pageSize;//每页大小
    private long total;//总数
    private int totalPage;//总页数
    private List<FPListPojo> list;
}
