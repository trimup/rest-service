package com.lihe.event;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * Created by trimup on 2016/8/8.
 */
@ApiModel
@Data
public class QueryHSProEvent {
    private String ofund_type;  //基金类型 8 	专户理财
//    2 	货币
//    9 	超短期理财产品
//    3 	ETF
//    0 	普通
//    4 	保本基金
//    1 	短债
//    A 	混合型基金
//    5 	QDII基金
    private Integer sort_type; //排序  0 日 1  月  2 季度  3半年 4 年
    private int page = 1;
    private int pageSize = 5;

    public boolean isCheck(){
        return ofund_type==null||sort_type==null;
    }
}
