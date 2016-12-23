package com.lihe.entity.productDetail;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by trimup on 2016/8/25.
 * 基金信息
 */
@Data
public class FundInfo {
       private Integer      id             ;   //  '主键ID',
       private String      fund_code   ;   //  '基金代码',
       private Date    create_time ;   //  '创建时间',
       private String  pu_confir_time;          //申购确认时间
       private String  redeem_confir_time;     //赎回确认时间
       private String  fund_manager_rate;      //基金管理费
       private String  fund_trust_rate;        //基金托管费率
       private String  sale_service_rate;      //销售服务费
       private String  setup_time;//成立时间
       private String  net_asset_value;   //   资产规模
       private String  fund_management;// 基金管理
       private String  custodian_bank;//托管银行
       private String total_share ;//    份额规模
       private String  invest_scope ;//投资范围
       private String  invest_target;//投资目标
       private Date    update_time ;  // 更新时间
       private String  fund_manager_logo ;   //基金经理头像
       private String   fund_manager_name;    //基金经理名字
       private String  fund_manager_introduce;  //基金经理介绍



}
