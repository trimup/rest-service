package com.lihe.pojo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by trimup on 2016/8/12.
 * 固定收益基金详情
 */
@Data
public class FixIncomeDetailPojo {
    private Integer  id;
    private String    product_code;//项目code
    private String     product_name         ;//项目名称
    private String     publish_agency       ;//发行机构
    private String     product_status    ;  //项目状态
    private String     product_deadline     ; //产品期限
    private String     raise_start_time     ;// 募资开始时间
    private BigDecimal product_min_invest   ; //项目起投金额
    private String     pay_interest_type ;//付息方式
    private BigDecimal collect_count   ;//项目融资规模
    private String     raise                ; //融资主体
    private String     profit_type       ;// 收益类型
    private String     raise_capital_account;//募集资金账户
    private String     fund_uses            ; //资金用途
    private String     payment_resource     ;//还款来源
    private String     rish_contro_step     ; //风控措施
    private String     product_excellence   ;//项目亮点
    private String     memo                 ;//项目补充说明
}
