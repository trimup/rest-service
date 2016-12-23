package com.lihe.entity;

import lombok.Data;

/**
 * Created by trimup on 2016/8/31.
 */
@Data
public class RecommendProductInfo {
    private Integer  id   ;          // id
    private String  pro_code;           //项目CODE
    private Integer  type;    // 1私募 2公募
    private Integer  sort ;     //排序
    private Integer  cate ;// 1首页主推 2热搜
    private String  create_time ;   //L创建时间
}
