package com.lihe.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by trimup on 2016/8/15.
 * 定向增发详情
 */
@Data
public class PrPlaceDetailPojo {
    private Integer     id                     ;
    private String  product_code;
    private String      product_name           ;  //项目名称
    private String      publish_agency         ;  //发行机构
    private String      product_status         ;  //项目状态
    private BigDecimal  product_min_invest     ;  //项目起投金额
    private String      pay_interest_type      ;  //付息方式
    private String      raise                  ;  //融资主体
    private String      fund_uses              ;  //资金用途
    private String      payment_resource       ;  //还款来源
    private BigDecimal collect_count   ;//项目融资规模
    private String      rish_contro_step       ; //风控措施
    private String      product_excellence     ;//项目亮点
    private String      memo                   ;//项目补充说明
    private String      project_forecast_time  ;//预计开放日
    private String      project_start_time     ;//产品成立时间
    private  BigDecimal product_management_fees;//项目管理费用
    private BigDecimal  revenue_share          ;//  收益分成

}
