package com.lihe.event;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.List;

/**
 * Created by trimup on 2016/8/25.
 *
 * 行情趋势Data
 */
@Data
public class FundTrendData {
    private List<String>  dateList;
    private List<String> netValueList;
}
