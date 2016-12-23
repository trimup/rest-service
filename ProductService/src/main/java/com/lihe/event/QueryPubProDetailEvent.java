package com.lihe.event;

import com.google.common.base.Strings;
import lombok.Data;

/**
 * Created by trimup on 2016/8/24.
 * 查询公募详情事件
 */
@Data
public class QueryPubProDetailEvent {
    String fund_code ;  //基金编号
    int page =1;
    int pageSize=50;


    public boolean isCheck(){
      return   Strings.isNullOrEmpty(fund_code);
    }
}
