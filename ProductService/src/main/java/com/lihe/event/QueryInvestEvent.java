package com.lihe.event;

import lombok.Data;

/**
 * Created by trimup on 2016/9/5.
 *
 */
@Data
public class QueryInvestEvent {
    private Integer user_tid;
    private String token ;
    private int page=1;
    private int pageSize=5;
}
