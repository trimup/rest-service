package com.lihe.pojo.productDetail;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by trimup on 2016/9/13.
 * 阳光私募详情
 */
@Data
public class SunProductPojo {
    private Integer     id                     ;
    private String      product_code;
    private String      product_name           ;  //项目名称
    private String      publish_agency         ;  //发行机构
    private String      product_status         ;  //项目状态
    private BigDecimal  product_min_invest     ;  //项目起投金额
    private String     net_val                ;  //最新净值
    private String     raiseAndFall          ;//近一年涨跌幅
    private String      raise                  ;  //融资主体
    private String      fund_uses              ;  //资金用途
    private String      payment_resource       ;  //还款来源
    private String      rish_contro_step       ; //风控措施
    private String      product_excellence     ;//项目亮点
    private String      memo                   ;//项目补充说明
    private String      project_forecast_time  ;//预计开放日
    private String      project_start_time     ;//产品成立时间
    private  BigDecimal product_management_fees;//项目管理费用
    private BigDecimal  revenue_share          ;//  收益分成
}
