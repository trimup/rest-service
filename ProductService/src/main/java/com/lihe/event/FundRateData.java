package com.lihe.event;

import com.lihe.pojo.productDetail.PurchaseRatePojo;
import com.lihe.pojo.productDetail.RedeemRatePojo;
import lombok.Data;

import java.util.List;

/**
 * Created by trimup on 2016/8/22.
 * 基金费率
 */
@Data
public class FundRateData {
    private String  pu_confir_time ="---";          //申购确认时间
    private String  redeem_confir_time;      //赎回确认时间
    private String  fund_manager_rate ="---";      //基金管理费
    private String  fund_trust_rate ="---";        //基金托管费率
    private String  sale_service_rate ="---"       ;      //销售服务费
    private List<PurchaseRatePojo> purchase_rate  ;//申购费率
    private List<RedeemRatePojo> redeem_rate  ;//赎回费率

}
