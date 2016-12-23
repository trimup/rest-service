package com.lihe.event;

import com.lihe.pojo.FundListPojo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by trimup on 2016/8/9.
 * 公募基金项目列表返回参数
 */
@ApiModel
@Data
public class HSProListData {

    private int page;//第几页
    private int pageSize;//每页大小
    private long total;//总数
    private int totalPage;//总页数
    @ApiModelProperty(required = true, name = "公募基金项目列表", value = "FundListPojo")
    private List<FundListPojo> list;
}
