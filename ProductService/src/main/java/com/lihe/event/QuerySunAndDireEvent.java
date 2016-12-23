package com.lihe.event;

import lombok.Data;

/**
 * Created by trimup on 2016/8/12.
 * 查询阳光私募和定向增发
 *
 */
@Data
public class QuerySunAndDireEvent {
    private Integer product_type; //10029代表阳光私募，10030代表定向增发
    private int page = 1;
    private int pageSize = 5;

    public boolean isCheck(){
        return  product_type==null;
    }
}
