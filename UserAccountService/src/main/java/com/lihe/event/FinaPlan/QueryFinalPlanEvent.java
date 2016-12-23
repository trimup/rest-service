package com.lihe.event.FinaPlan;

import lombok.Data;

/**
 * Created by trimup on 2016/9/27.
 */
@Data
public class QueryFinalPlanEvent {
    private int page=1;
    private int pageSize=5;
    private Integer fp_id;


    public boolean orCheck(){
        return  fp_id==null;
    }
}
