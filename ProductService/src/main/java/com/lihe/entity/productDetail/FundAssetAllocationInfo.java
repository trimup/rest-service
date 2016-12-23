package com.lihe.entity.productDetail;

import lombok.Data;

import java.util.Date;

/**
 * Created by trimup on 2016/12/22.
 * 基金资产配置
 */
@Data
public class FundAssetAllocationInfo {
            private Integer  id             ;   //    '主键ID',
            private String  fund_code      ;   //    '基金代码',
            private String  hqrq           ;   //    行情日期',
            private String  stock_per      ;   //     '资产股票占比',
            private String  bank_per       ;   //    '资产银行占比',
            private String  bond_per       ;   //    '资产债权占比',
            private String  total_assets   ;   //    '总资产',
            private Date    create_time    ;   //    '创建时间',
            private Date    update_time    ;   //     '更新时间',
}
