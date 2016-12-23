package com.lihe.event;

import lombok.Data;

/**
 * Created by trimup on 2016/8/12.
 */
@Data
public class QueryFixIncomEvent {
    private int page = 1;
    private int pageSize = 5;
}
