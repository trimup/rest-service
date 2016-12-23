package com.lihe.pojo.productDetail;

import com.lihe.entity.productDetail.FundMainPositionsInfo;
import com.lihe.pojo.AssetAllocationPojo;
import com.lihe.pojo.MainPositionsPojo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by trimup on 2016/8/22.
 */
@Data
public class PubProductBasePojo {
    private String     fund_full_name	    ;   //基金全称
    private String     fund_code 	        ;   //基金代码
    private String     ofund_type	        ;   //基金类别  字典[基金类型]
    private Integer    ofund_risklevel	    ;   //       基金风险等级	N	8	0	N	v4.0.0.0
    private String   ofundRisk ;         //基金风险等级
    private String     fund_status	        ;   //基金状态
    private String     custodial_bank        ;   //托管银行
    private String     setup_time              ;  //成立时间
    private String     net_asset_value;         //资产净值
    private String     fund_share;              //基金总份额
    private AssetAllocationPojo assetAllocationPojo;//  资产配置
    private List<FundMainPositionsInfo> mainPositionsList;// 主要持仓
    private String  fund_management;// 基金管理
    private String tzfw;   //投资范围
    private String tzmb;  //投资



}
