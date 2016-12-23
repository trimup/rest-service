package com.lihe.entity.productDetail;

import lombok.Data;

/**
 * Created by trimup on 2016/8/26.
 */
@Data
public class FundMainPositionsInfo {
    private  Integer    id               ;  // 主键ID',
    private  String    fund_code        ;  // 基金代码',
    private  String    name             ;  // '名称',
    private  String    code            ;  // '代码',
    private  String    capital         ;  // '市值',
    private  String    accounting_ratio       ;  // '占配比',
    private  String    create_time     ;  //  '创建时间',

}
