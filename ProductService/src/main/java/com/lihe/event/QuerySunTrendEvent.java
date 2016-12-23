package com.lihe.event;

import lombok.Data;

/**
 * Created by trimup on 2016/9/13.
 */
@Data
public class QuerySunTrendEvent {
    private Integer product_id;
    private Integer time_limit;


    public boolean  isCheck(){
        return  product_id==null||time_limit==null;
    }
}
