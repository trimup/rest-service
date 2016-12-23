package com.lihe.hundSunreply;

import com.lihe.pojo.FundMarketPojo;
import lombok.Data;

import java.util.List;

/**
 * Created by trimup on 2016/8/23.
 */
@Data
public class FundMarketQueryReply {
    String code;
    String message;
    int rowcount;
    int total_count;
    List<FundMarketPojo> results;
}
