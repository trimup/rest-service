package com.lihe.entity.productDetail;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by trimup on 2016/9/22.
 */
@Data
public class FundIncomeInfo {
      private String  fund_code     ;  //      '基金代码',
      private Integer  user_tid      ;  //      '用户id',
      private BigDecimal income        ;  //      '收益',
      private String  income_date   ;  //      '收益日期',
}
