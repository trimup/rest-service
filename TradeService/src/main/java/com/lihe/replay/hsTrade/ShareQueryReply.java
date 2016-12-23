package com.lihe.replay.hsTrade;

import lombok.Data;

import java.util.List;

/**
 * Created by trimup on 2016/10/14.
 */
@Data
public class ShareQueryReply {
    private String  code;
    private String message;
    private Integer rowcount;
    private Integer total_count;
    private List<ShareQueryPo> shareQuerys;
}
