package com.lihe.event;

import com.lihe.pojo.FundHistoryPojo;
import com.lihe.pojo.FundListPojo;
import com.lihe.pojo.productDetail.FundNetDayPojo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by trimup on 2016/8/29.
 * 历史基金详情返回参数
 */
@Data
public class FundHistoryNetData {
    private int page;//第几页
    private int pageSize;//每页大小
    private long total;//总数
    private int totalPage;//总页数
    private List<FundNetDayPojo> list;
}
